import java.util.*;

public class ClinicaServico {

    // ArrayList<Consulta>/Atendimento/Pagamento: ordem de inserção importa e
    // acesso por índice é necessário (relatórios e operações referenciam consultas
    // pelo índice digitado pelo usuário)
    // HashMap<String, Paciente>/<String, Profissional>: busca por chave (CPF
    // ou nome) é a operação mais frequente aqui (agendar, pagar, atualizar
    // sempre partem de um identificador conhecido) — muito mais eficiente
    // que percorrer uma lista inteira a cada busca
    private Map<String, Paciente> pacientesPorCpf;
    private Map<String, Profissional> profissionaisPorNome;

    private List<Consulta> consultas;
    private List<Atendimento> atendimentos;
    private List<Pagamento> pagamentos;

    // HashSet<String>: só precisamos verificar existência (contains) de CPFs
    // já cadastrados; não importa ordem nem temos acesso indexado
    private Set<String> cpfsCadastrados;
    private List<Double> multas;

    public ClinicaServico() {
        this.pacientesPorCpf = new HashMap<>();
        this.profissionaisPorNome = new HashMap<>();

        this.consultas = new ArrayList<>();
        this.atendimentos = new ArrayList<>();
        this.pagamentos = new ArrayList<>();

        this.cpfsCadastrados = new HashSet<>();
        this.multas = new ArrayList<>();
    }

    // ====================
    // PACIENTES
    // ====================

    public boolean cadastrarPaciente(Paciente paciente) {

        if (paciente == null || cpfsCadastrados.contains(paciente.getCpf())) {
            return false;
        }

        pacientesPorCpf.put(paciente.getCpf(), paciente);
        cpfsCadastrados.add(paciente.getCpf());

        return true;
    }

    public Paciente buscarPaciente(String cpf) throws PacienteNaoEncontradoException {
        Paciente paciente = pacientesPorCpf.get(cpf);

        if (paciente == null) {
            throw new PacienteNaoEncontradoException("Paciente com CPF " + cpf + " nao encontrado.");
        }

        return paciente;
    }

    public void desativarPaciente(String cpf) throws PacienteNaoEncontradoException {
        Paciente paciente = buscarPaciente(cpf);
        paciente.desativar();
    }

    public List<Paciente> listarPacientes() {
        return new ArrayList<>(pacientesPorCpf.values());
    }

    // ====================
    // PROFISSIONAIS
    // ====================

    public boolean cadastrarProfissional(Profissional profissional) {

        if (profissional == null || profissionaisPorNome.containsKey(profissional.getNome())) {
            return false;
        }

        profissionaisPorNome.put(profissional.getNome(), profissional);
        return true;
    }

    public Profissional buscarProfissional(String nome) throws ProfissionalNaoEncontradoException {
        Profissional profissional = profissionaisPorNome.get(nome);

        if (profissional == null) {
            throw new ProfissionalNaoEncontradoException("Profissional " + nome + " nao encontrado.");
        }

        return profissional;
    }

    public List<Profissional> buscarPorEspecialidade(String especialidade) {

        List<Profissional> resultado = new ArrayList<>();

        for (Profissional p : profissionaisPorNome.values()) {
            if (p.getEspecialidade().equalsIgnoreCase(especialidade)) {
                resultado.add(p);
            }
        }

        return resultado;
    }

    public List<Profissional> listarProfissionais() {
        return new ArrayList<>(profissionaisPorNome.values());
    }

    /**
     * Combina pacientes e profissionais numa única lista de Pessoa, para
     * relatórios que percorrem o cadastro completo de forma polimórfica
     * (cada Pessoa sabe exibir seu próprio resumo, seja Paciente ou alguma
     * subclasse de Profissional).
     */
    public List<Pessoa> listarTodasPessoas() {
        List<Pessoa> todas = new ArrayList<>();
        todas.addAll(pacientesPorCpf.values());
        todas.addAll(profissionaisPorNome.values());
        return todas;
    }

    /**
     * Procura, entre os profissionais de uma especialidade, o primeiro que
     * tenha valor de consulta definido, atenda no dia da semana E no turno
     * do horário pedido, e esteja livre nesse horário.
     */
    public Profissional buscarProfissionalDisponivel(String especialidade, String diaSemana,
                                                      String data, String horario)
            throws HorarioIndisponivelException {

        String turno = HorarioDisponivel.turnoDoHorario(horario);

        for (Profissional p : profissionaisPorNome.values()) {
            if (p.getEspecialidade().equals(especialidade)
                    && p.getValorConsulta() > 0
                    && p.atendeNoDiaETurno(diaSemana, turno)
                    && !temConflito(p.getNome(), data, horario)) {
                return p;
            }
        }

        throw new HorarioIndisponivelException(
                "Nenhum profissional de " + especialidade + " disponivel nesse dia/horario.");
    }

    // ====================
    // CONSULTAS
    // ====================

    /**
     * Valida e agenda a consulta, lançando a exceção específica que corresponde
     * ao motivo da falha (paciente inativo, paciente/profissional inexistente,
     * ou horário indisponível).
     */
    public void agendarConsulta(Consulta consulta)
            throws PacienteNaoEncontradoException, PacienteInativoException,
                   ProfissionalNaoEncontradoException, HorarioIndisponivelException {

        Paciente paciente = buscarPaciente(consulta.getCpf());

        if (!paciente.isAtivo()) {
            throw new PacienteInativoException(
                    "Paciente " + paciente.getNome() + " esta inativo. Nao e possivel agendar.");
        }

        buscarProfissional(consulta.getNomeProfissional());

        if (temConflito(consulta.getNomeProfissional(), consulta.getData(), consulta.getHorario())) {
            throw new HorarioIndisponivelException("Horario ja ocupado para esse profissional.");
        }

        consultas.add(consulta);
    }

    /**
     * Adiciona a consulta diretamente, sem refazer as validações de
     * agendarConsulta. Útil quando essas validações já foram feitas
     * antes (ex: remarcação, agendamento por especialidade).
     */
    public void adicionarConsulta(Consulta consulta) {
        consultas.add(consulta);
    }

    public void cancelarConsulta(int indice) throws ConsultaNaoEncontradaException, OperacaoInvalidaException {

        Consulta consulta = getConsulta(indice);

        if (consulta.isConcluida()) {
            throw new OperacaoInvalidaException("Consulta ja realizada. Nao pode cancelar.");
        }
        if ("cancelada".equals(consulta.getStatus())) {
            throw new OperacaoInvalidaException("Consulta ja cancelada.");
        }

        consulta.cancelar();
    }

    public List<Consulta> listarConsultas() {
        return consultas;
    }

    public int getTotalConsultas() {
        return consultas.size();
    }

    public Consulta getConsulta(int indice) throws ConsultaNaoEncontradaException {
        if (indice < 0 || indice >= consultas.size()) {
            throw new ConsultaNaoEncontradaException("Consulta de indice " + indice + " nao encontrada.");
        }
        return consultas.get(indice);
    }

    /**
     * Procura o índice da consulta que casa com cpf, data e horário
     * informados.
     */
    public int buscarIndiceConsulta(String cpf, String data, String horario)
            throws ConsultaNaoEncontradaException {

        for (int i = 0; i < consultas.size(); i++) {
            Consulta c = consultas.get(i);
            if (c.getCpf().equals(cpf) && c.getData().equals(data) && c.getHorario().equals(horario)) {
                return i;
            }
        }

        throw new ConsultaNaoEncontradaException(
                "Consulta nao encontrada para CPF " + cpf + " em " + data + " " + horario + ".");
    }

    /**
     * Procura o índice de uma consulta AGENDADA que casa com cpf, data
     * e horário originais — usado na remarcação.
     */
    public int buscarIndiceConsultaAgendada(String cpf, String data, String horario)
            throws ConsultaNaoEncontradaException {

        for (int i = 0; i < consultas.size(); i++) {
            Consulta c = consultas.get(i);
            if (c.getCpf().equals(cpf) && c.getData().equals(data) && c.getHorario().equals(horario)
                    && "agendada".equalsIgnoreCase(c.getStatus())) {
                return i;
            }
        }

        throw new ConsultaNaoEncontradaException(
                "Consulta agendada nao encontrada para CPF " + cpf + " em " + data + " " + horario + ".");
    }

    public List<Consulta> buscarConsultasPorPaciente(String cpf) {

        List<Consulta> resultado = new ArrayList<>();

        for (Consulta c : consultas) {
            if (c.getCpf().equals(cpf)) {
                resultado.add(c);
            }
        }

        return resultado;
    }

    // ====================
    // ATENDIMENTOS
    // ====================

    public void registrarAtendimento(Atendimento atendimento) {
        atendimentos.add(atendimento);
    }

    public List<Atendimento> listarAtendimentos() {
        return atendimentos;
    }

    public int getTotalAtendimentos() {
        return atendimentos.size();
    }

    public Atendimento getUltimoAtendimento() {
        if (atendimentos.isEmpty()) {
            return null;
        }
        return atendimentos.get(atendimentos.size() - 1);
    }

    // ====================
    // PAGAMENTOS
    // ====================

    public void registrarPagamento(Pagamento pagamento) {
        pagamentos.add(pagamento);
    }

    public List<Pagamento> listarPagamentos() {
        return pagamentos;
    }

    public int getTotalPagamentos() {
        return pagamentos.size();
    }

    // ====================
    // MULTAS
    // ====================

    public void registrarMulta(double valor) {
        multas.add(valor);
    }

    public double calcularTotalMultas() {

        double total = 0;

        for (double m : multas) {
            total += m;
        }

        return total;
    }

    public List<Double> listarMultas() {
        return multas;
    }

    // ====================
    // REGRAS DE NEGÓCIO
    // ====================

    public boolean temConflito(String nomeProfissional,
                               String data,
                               String horario) {

        for (Consulta consulta : consultas) {

            if (consulta.getNomeProfissional().equals(nomeProfissional)
                    && consulta.getData().equals(data)
                    && consulta.getHorario().equals(horario)
                    && "agendada".equalsIgnoreCase(consulta.getStatus())) {

                return true;
            }
        }

        return false;
    }

    /**
     * Sugere um horário livre dentro do turno em que o profissional realmente
     * atende naquele dia (manha ou tarde), evitando sugerir um horário fora
     * da disponibilidade cadastrada.
     */
    public String sugerirHorario(String nomeProfissional, String data, String diaSemana)
            throws ProfissionalNaoEncontradoException {

        Profissional profissional = buscarProfissional(nomeProfissional);

        for (int hora = 8; hora <= 18; hora++) {

            String horario = (hora < 10) ? "0" + hora + ":00" : hora + ":00";
            String turno = HorarioDisponivel.turnoDoHorario(horario);

            if (profissional.atendeNoDiaETurno(diaSemana, turno)
                    && !temConflito(nomeProfissional, data, horario)) {
                return horario;
            }
        }

        return "";
    }
}

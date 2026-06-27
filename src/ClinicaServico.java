import java.util.*;

public class ClinicaServico {

    // ====================
    // ESTRUTURAS (REVISADAS)
    // ====================

    private Map<String, Paciente> pacientesPorCpf;
    private Map<String, Profissional> profissionaisPorNome;

    private List<Consulta> consultas;
    private List<Atendimento> atendimentos;
    private List<Pagamento> pagamentos;

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

    public Paciente buscarPaciente(String cpf) {
        return pacientesPorCpf.get(cpf);
    }

    public boolean desativarPaciente(String cpf) {
        Paciente paciente = buscarPaciente(cpf);

        if (paciente == null) {
            return false;
        }

        paciente.desativar();
        return true;
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

    public Profissional buscarProfissional(String nome) {
        return profissionaisPorNome.get(nome);
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
     * Procura, entre os profissionais de uma especialidade, o primeiro que
     * tenha valor de consulta definido, atenda no dia da semana E no turno
     * do horário pedido, e esteja livre nesse horário.
     */
    public Profissional buscarProfissionalDisponivel(String especialidade, String diaSemana,
                                                      String data, String horario) {

        String turno = HorarioDisponivel.turnoDoHorario(horario);

        for (Profissional p : profissionaisPorNome.values()) {
            if (p.getEspecialidade().equals(especialidade)
                    && p.getValorConsulta() > 0
                    && p.atendeNoDiaETurno(diaSemana, turno)
                    && !temConflito(p.getNome(), data, horario)) {
                return p;
            }
        }

        return null;
    }

    // ====================
    // CONSULTAS
    // ====================

    public boolean agendarConsulta(Consulta consulta) {

        Paciente paciente = buscarPaciente(consulta.getCpf());
        if (paciente == null || !paciente.isAtivo()) {
            return false;
        }

        Profissional profissional = buscarProfissional(consulta.getNomeProfissional());
        if (profissional == null) {
            return false;
        }

        if (temConflito(
                consulta.getNomeProfissional(),
                consulta.getData(),
                consulta.getHorario())) {
            return false;
        }

        consultas.add(consulta);
        return true;
    }

    /**
     * Adiciona a consulta diretamente, sem refazer as validações de
     * agendarConsulta. Útil quando essas validações já foram feitas
     * antes (ex: remarcação, agendamento por especialidade).
     */
    public void adicionarConsulta(Consulta consulta) {
        consultas.add(consulta);
    }

    public boolean cancelarConsulta(int indice) {

        if (indice < 0 || indice >= consultas.size()) {
            return false;
        }

        consultas.get(indice).cancelar();
        return true;
    }

    public List<Consulta> listarConsultas() {
        return consultas;
    }

    public int getTotalConsultas() {
        return consultas.size();
    }

    public Consulta getConsulta(int indice) {
        if (indice < 0 || indice >= consultas.size()) {
            return null;
        }
        return consultas.get(indice);
    }

    /**
     * Procura o índice da consulta que casa com cpf, data e horário
     * informados. Retorna -1 se não encontrar.
     */
    public int buscarIndiceConsulta(String cpf, String data, String horario) {

        for (int i = 0; i < consultas.size(); i++) {
            Consulta c = consultas.get(i);
            if (c.getCpf().equals(cpf) && c.getData().equals(data) && c.getHorario().equals(horario)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Procura o índice de uma consulta AGENDADA que casa com cpf, data
     * e horário originais — usado na remarcação.
     */
    public int buscarIndiceConsultaAgendada(String cpf, String data, String horario) {

        for (int i = 0; i < consultas.size(); i++) {
            Consulta c = consultas.get(i);
            if (c.getCpf().equals(cpf) && c.getData().equals(data) && c.getHorario().equals(horario)
                    && "agendada".equalsIgnoreCase(c.getStatus())) {
                return i;
            }
        }

        return -1;
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
    public String sugerirHorario(String nomeProfissional, String data, String diaSemana) {

        Profissional profissional = buscarProfissional(nomeProfissional);
        if (profissional == null) {
            return "";
        }

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
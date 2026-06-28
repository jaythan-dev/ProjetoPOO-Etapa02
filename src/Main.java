import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static ClinicaServico servico = new ClinicaServico();
    static Scanner sc = new Scanner(System.in);

    private static final String[] DIAS_VALIDOS =
            {"sabado", "domingo", "segunda", "terca", "quarta", "quinta", "sexta"};

    // Centraliza a leitura de números inteiros: nunca deixa NumberFormatException
    // escapar para quem chamou, sempre pede de novo até receber um valor válido.
    private static int lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida! Digite um numero inteiro.");
            }
        }
    }

    // Mesma ideia de lerInteiro, mas para valores decimais (valores monetários etc).
    private static double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida! Digite um numero (ex: 150.50).");
            }
        }
    }

    // Lê uma data no formato DD/MM/AAAA, validando o formato antes de aceitar
    // (sem isso, descobrirDiaSemana lançaria exceção não tratada com data malformada).
    private static String lerData(String prompt) {
        while (true) {
            System.out.print(prompt);
            String data = sc.nextLine();
            if (dataValida(data)) {
                return data;
            }
            System.out.println("Data invalida! Use o formato DD/MM/AAAA.");
        }
    }

    private static boolean dataValida(String data) {
        if (data.length() != 10 || data.charAt(2) != '/' || data.charAt(5) != '/') {
            return false;
        }
        try {
            int dia = Integer.parseInt(data.substring(0, 2));
            int mes = Integer.parseInt(data.substring(3, 5));
            Integer.parseInt(data.substring(6, 10));
            return dia >= 1 && dia <= 31 && mes >= 1 && mes <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Lê um horário no formato HH:MM, validando o formato antes de aceitar.
    private static String lerHorario(String prompt) {
        while (true) {
            System.out.print(prompt);
            String horario = sc.nextLine();
            if (horarioValido(horario)) {
                return horario;
            }
            System.out.println("Horario invalido! Use o formato HH:MM.");
        }
    }

    private static boolean horarioValido(String horario) {
        if (horario.length() != 5 || horario.charAt(2) != ':') {
            return false;
        }
        try {
            int hora = Integer.parseInt(horario.substring(0, 2));
            int minuto = Integer.parseInt(horario.substring(3, 5));
            return hora >= 0 && hora <= 23 && minuto >= 0 && minuto <= 59;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=== CLINICA VIDAPLENA ===");
            System.out.println("1 - Pacientes");
            System.out.println("2 - Profissionais");
            System.out.println("3 - Consultas");
            System.out.println("4 - Atendimentos");
            System.out.println("5 - Pagamentos");
            System.out.println("6 - Relatorios");
            System.out.println("0 - Sair");

            opcao = lerInteiro("Escolha: ");

            switch (opcao) {
                case 1 -> menuPacientes();
                case 2 -> menuProfissionais();
                case 3 -> menuConsultas();
                case 4 -> menuAtendimentos();
                case 5 -> menuPagamentos();
                case 6 -> menuRelatorios();
                case 0 -> System.out.println("Sistema encerrado.");
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    // =========================
    // PACIENTES (REFATORADO)
    // =========================

    public static void menuPacientes() {
        int op = -1;

        while (op != 0) {
            System.out.println("\n--- PACIENTES ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Complementar cadastro");
            System.out.println("3 - Buscar por CPF");
            System.out.println("4 - Listar todos");
            System.out.println("5 - Desativar");
            System.out.println("0 - Voltar");

            op = lerInteiro("Escolha: ");

            switch (op) {
                case 1 -> cadastrarPaciente();
                case 2 -> complementarPaciente();
                case 3 -> buscarPaciente();
                case 4 -> listarPacientes();
                case 5 -> desativarPaciente();
            }
        }
    }

    public static void cadastrarPaciente() {

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        Paciente paciente = new Paciente(nome, cpf);

        int tipo = lerInteiro("Tipo (1-Minimo / 2-Completo): ");

        if (tipo >= 2) {
            paciente.setIdade(lerInteiro("Idade: "));

            System.out.print("Telefone: ");
            paciente.setTelefone(sc.nextLine());
        }

        if (tipo == 3) {
            paciente.setConvenio(escolherConvenio());
        }

        if (servico.cadastrarPaciente(paciente)) {
            System.out.println("Paciente cadastrado!");
        } else {
            System.out.println("CPF já cadastrado!");
        }
    }

    // =========================
    // ESCOLHA DE CONVENIO (3 CONVENIOS FIXOS DA CLINICA)
    // =========================

    private static Convenio escolherConvenio() {
        System.out.println("Convenios disponiveis:");
        System.out.println("1 - SaudePlus (40% de cobertura)");
        System.out.println("2 - VidaMais (30% de cobertura)");
        System.out.println("3 - BemEstar (50% de cobertura)");
        int opcao = lerInteiro("Escolha o convenio: ");

        return switch (opcao) {
            case 1 -> Convenio.saudePlus();
            case 2 -> Convenio.vidaMais();
            case 3 -> Convenio.bemEstar();
            default -> {
                System.out.println("Opcao invalida, nenhum convenio sera associado.");
                yield null;
            }
        };
    }

    public static void complementarPaciente() {

        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        try {
            Paciente paciente = servico.buscarPaciente(cpf);

            paciente.setIdade(lerInteiro("Idade: "));

            System.out.print("Telefone: ");
            paciente.setTelefone(sc.nextLine());

            int tipo = lerInteiro("Convenio? (2-Sim / 1-Nao): ");

            if (tipo == 2) {
                paciente.setConvenio(escolherConvenio());
            }

            System.out.println("Cadastro atualizado!");

        } catch (PacienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void buscarPaciente() {

        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        try {
            Paciente paciente = servico.buscarPaciente(cpf);
            System.out.println(paciente.exibirResumo());
        } catch (PacienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void listarPacientes() {

        List<Paciente> lista = servico.listarPacientes();

        if (lista.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
            return;
        }

        for (Paciente p : lista) {
            System.out.println(p.exibirResumo());
        }
    }

    public static void desativarPaciente() {

        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        try {
            servico.desativarPaciente(cpf);
            System.out.println("Paciente desativado.");
        } catch (PacienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }
    // ---- PROFISSIONAIS ----

    public static void menuProfissionais() {

        int op = -1;

        while (op != 0) {

            System.out.println("\n--- PROFISSIONAIS ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Atualizar");
            System.out.println("3 - Listar todos");
            System.out.println("4 - Filtrar por especialidade");
            System.out.println("0 - Voltar");

            op = lerInteiro("Escolha: ");

            switch (op) {
                case 1 -> cadastrarProfissional();
                case 2 -> atualizarProfissional();
                case 3 -> listarProfissionais();
                case 4 -> filtrarProfissionais();
            }
        }
    }

    // =========================
    // CADASTRAR PROFISSIONAL
    // =========================

    public static void cadastrarProfissional() {

        try {
            System.out.print("Nome: ");
            String nome = sc.nextLine();

            System.out.print("CPF: ");
            String cpf = sc.nextLine();

            try {
                servico.buscarProfissional(nome);
                System.out.println("Profissional já cadastrado!");
                return;
            } catch (ProfissionalNaoEncontradoException e) {
                System.out.println(e.getMessage() + " Nome livre, seguindo com o cadastro.");
            }

            System.out.println("Tipo de profissional:");
            System.out.println("1 - Fisioterapeuta");
            System.out.println("2 - Psicologo");
            System.out.println("3 - Nutricionista");
            System.out.println("4 - Clinico Geral");
            int tipoProfissional = lerInteiro("Escolha o tipo: ");

            int completo = lerInteiro("Cadastro completo agora? (1-Nao / 2-Sim): ");

            Profissional prof;

            if (completo == 1) {
                prof = criarProfissionalBasico(tipoProfissional, nome, cpf);
            } else {
                System.out.print("Registro: ");
                String reg = sc.nextLine();

                double valor = lerDouble("Valor consulta: ");

                String infoEspecifica = lerInformacaoEspecifica(tipoProfissional);

                prof = criarProfissionalCompleto(tipoProfissional, nome, cpf, reg, valor, infoEspecifica);

                int informarDias = lerInteiro("Informar dias de atendimento agora? (1-Nao / 2-Sim): ");

                if (informarDias == 2) {
                    List<HorarioDisponivel> horarios = lerHorariosDisponiveis();
                    prof.atualizar(reg, valor, horarios);
                }
            }

            if (prof == null) {
                System.out.println("Tipo de profissional invalido.");
                return;
            }

            boolean ok = servico.cadastrarProfissional(prof);

            if (ok) {
                System.out.println("Profissional cadastrado!");
            } else {
                System.out.println("Erro ao cadastrar profissional.");
            }

        } finally {
            System.out.println("--- operação finalizada ---");
        }
    }

    // Cria o profissional só com nome/cpf (cadastro básico, sem registro/valor)
    private static Profissional criarProfissionalBasico(int tipoProfissional, String nome, String cpf) {
        return switch (tipoProfissional) {
            case 1 -> new Fisioterapeuta(nome, cpf);
            case 2 -> new Psicologo(nome, cpf);
            case 3 -> new Nutricionista(nome, cpf);
            case 4 -> new ClinicoGeral(nome, cpf);
            default -> null;
        };
    }

    // Cria o profissional já com registro, valor e a informação específica da especialidade
    private static Profissional criarProfissionalCompleto(int tipoProfissional, String nome, String cpf,
                                                            String reg, double valor, String infoEspecifica) {
        return switch (tipoProfissional) {
            case 1 -> new Fisioterapeuta(nome, cpf, reg, valor, Integer.parseInt(infoEspecifica));
            case 2 -> new Psicologo(nome, cpf, reg, valor, infoEspecifica);
            case 3 -> new Nutricionista(nome, cpf, reg, valor, infoEspecifica);
            case 4 -> new ClinicoGeral(nome, cpf, reg, valor, infoEspecifica);
            default -> null;
        };
    }

    // Pergunta o dado específico de cada especialidade (sessões, abordagem, plano, encaminhamento).
    // Para Fisioterapeuta, o valor já é validado como número aqui (lerInteiro), e devolvido como
    // String só para manter a mesma assinatura usada pelas outras especialidades.
    private static String lerInformacaoEspecifica(int tipoProfissional) {
        switch (tipoProfissional) {
            case 1:
                return String.valueOf(lerInteiro("Total de sessoes previstas: "));
            case 2:
                System.out.print("Abordagem terapeutica (ex: TCC, psicanalise, humanista): ");
                return sc.nextLine();
            case 3:
                System.out.print("Plano alimentar: ");
                return sc.nextLine();
            case 4:
                System.out.print("Encaminhamento: ");
                return sc.nextLine();
            default:
                return "";
        }
    }

    // =========================
    // ATUALIZAR PROFISSIONAL
    // =========================

    public static void atualizarProfissional() {

        System.out.print("Nome do profissional: ");
        String nome = sc.nextLine();

        try {
            Profissional prof = servico.buscarProfissional(nome);

            System.out.print("Registro: ");
            String reg = sc.nextLine();

            double valor = lerDouble("Valor consulta: ");

            int atualizarDias = lerInteiro("Atualizar dias de atendimento tambem? (1-Nao / 2-Sim): ");

            if (atualizarDias == 2) {
                List<HorarioDisponivel> horarios = lerHorariosDisponiveis();
                prof.atualizar(reg, valor, horarios);
            } else {
                prof.atualizar(reg, valor);
            }

            System.out.println("Profissional atualizado!");

        } catch (ProfissionalNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    // =========================
    // LISTAR PROFISSIONAIS
    // =========================

    public static void listarProfissionais() {

        var lista = servico.listarProfissionais();

        if (lista.isEmpty()) {
            System.out.println("Nenhum profissional cadastrado.");
            return;
        }

        for (Profissional p : lista) {
            System.out.println(p.exibirResumo());
        }
    }

    // =========================
    // FILTRAR POR ESPECIALIDADE
    // =========================

    public static void filtrarProfissionais() {

        System.out.print("Especialidade: ");
        String esp = sc.nextLine();

        var lista = servico.buscarPorEspecialidade(esp);

        if (lista.isEmpty()) {
            System.out.println("Nenhum profissional encontrado.");
            return;
        }

        for (Profissional p : lista) {
            System.out.println(p.exibirResumo());
        }
    }

    // =========================
    // LEITURA DOS HORARIOS DE ATENDIMENTO (DIA + TURNO)
    // =========================

    private static final String[] TURNOS_VALIDOS = {"manha", "tarde"};

    private static boolean diaValido(String dia) {
        for (String d : DIAS_VALIDOS) {
            if (d.equals(dia)) {
                return true;
            }
        }
        return false;
    }

    private static boolean turnoValido(String turno) {
        for (String t : TURNOS_VALIDOS) {
            if (t.equals(turno)) {
                return true;
            }
        }
        return false;
    }

    public static List<HorarioDisponivel> lerHorariosDisponiveis() {

        List<HorarioDisponivel> horarios = new ArrayList<>();
        String dia = "";

        System.out.println("Dias validos: sabado, domingo, segunda, terca, quarta, quinta, sexta");
        System.out.println("Turnos validos: manha, tarde");

        while (!dia.equals("fim")) {
            System.out.print("Dia de atendimento (ou 'fim'): ");
            dia = sc.nextLine();

            if (dia.equals("fim")) {
                break;
            }

            if (!diaValido(dia)) {
                System.out.println("Dia invalido, tente novamente.");
                continue;
            }

            System.out.print("Turno (manha/tarde): ");
            String turno = sc.nextLine();

            if (!turnoValido(turno)) {
                System.out.println("Turno invalido, tente novamente.");
                continue;
            }

            HorarioDisponivel horario = new HorarioDisponivel(dia, turno);

            if (horarios.contains(horario)) {
                System.out.println("Esse dia e turno ja foram informados.");
                continue;
            }

            horarios.add(horario);
        }

        return horarios;
    }

    // ---- CONSULTAS ----

    public static void menuConsultas() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- CONSULTAS ---");
            System.out.println("1 - Agendar (escolher profissional)");
            System.out.println("2 - Agendar (busca por especialidade)");
            System.out.println("3 - Cancelar");
            System.out.println("4 - Remarcar");
            System.out.println("5 - Listar todas");
            System.out.println("6 - Buscar por CPF");
            System.out.println("0 - Voltar");
            op = lerInteiro("Opcao: ");

            switch (op) {
                case 1: agendarComProfissional(); break;
                case 2: agendarPorEspecialidade(); break;
                case 3: cancelarConsulta(); break;
                case 4: remarcarConsulta(); break;
                case 5: listarConsultas(); break;
                case 6: buscarConsultasPorPaciente(); break;
                case 0: break;
                default: System.out.println("Opcao invalida!"); break;
            }
        }
    }

    public static void agendarComProfissional() {
        System.out.print("CPF do paciente: ");
        String cpf = sc.nextLine();

        try {
            Paciente paciente = servico.buscarPaciente(cpf);

            if (!paciente.isAtivo()) {
                throw new PacienteInativoException("Paciente inativo. Nao e possivel agendar.");
            }

            System.out.print("Nome do profissional: ");
            String nomeProf = sc.nextLine();
            Profissional profissional = servico.buscarProfissional(nomeProf);

            if (profissional.getValorConsulta() == 0) {
                System.out.println("Profissional sem valor definido. Nao pode agendar.");
                return;
            }

            String data = lerData("Data (DD/MM/AAAA): ");
            String horario = lerHorario("Horario (HH:MM): ");

            // verifica dia da semana e turno
            String diaSemana = descobrirDiaSemana(data);
            String turno = HorarioDisponivel.turnoDoHorario(horario);

            if (turno.equals(HorarioDisponivel.FORA_DO_EXPEDIENTE)) {
                throw new HorarioIndisponivelException("Horario fora do expediente da clinica (08:00 as 18:00).");
            }

            if (!profissional.atendeNoDiaETurno(diaSemana, turno)) {
                throw new HorarioIndisponivelException("Profissional nao atende nesse dia/turno.");
            }

            // verifica conflito
            if (servico.temConflito(nomeProf, data, horario)) {
                System.out.println("Horario ocupado!");
                String sugestao = servico.sugerirHorario(nomeProf, data, diaSemana);
                if (sugestao.isEmpty()) {
                    System.out.println("Nenhum horario disponivel nesse dia.");
                    return;
                }
                int aceita = lerInteiro("Sugestao: " + sugestao + ". Aceita? (1-Sim / 2-Nao): ");
                if (aceita == 1) {
                    horario = sugestao;
                } else {
                    return;
                }
            }

            int infoTipo = lerInteiro("Informar tipo? (1-Nao / 2-Sim): ");

            Consulta consulta;
            if (infoTipo == 1) {
                consulta = new Consulta(cpf, paciente.getNome(), nomeProf, data, horario);
            } else {
                System.out.print("Tipo (inicial/retorno/avaliacao): ");
                String tipo = sc.nextLine();
                consulta = new Consulta(cpf, paciente.getNome(), nomeProf, data, horario, tipo);
            }

            servico.adicionarConsulta(consulta);
            System.out.println("Consulta agendada com sucesso!");

        } catch (PacienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (ProfissionalNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (PacienteInativoException e) {
            System.out.println(e.getMessage());
        } catch (HorarioIndisponivelException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void agendarPorEspecialidade() {
        System.out.print("CPF do paciente: ");
        String cpf = sc.nextLine();
        Paciente paciente;
        try {
            paciente = servico.buscarPaciente(cpf);
        } catch (PacienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (!paciente.isAtivo()) {
            System.out.println("Paciente inativo. Nao e possivel agendar.");
            return;
        }

        System.out.print("Especialidade: ");
        String esp = sc.nextLine();
        String data = lerData("Data (DD/MM/AAAA): ");
        String horario = lerHorario("Horario (HH:MM): ");

        String diaSemana = descobrirDiaSemana(data);

        if (HorarioDisponivel.turnoDoHorario(horario).equals(HorarioDisponivel.FORA_DO_EXPEDIENTE)) {
            System.out.println("Horario fora do expediente da clinica (08:00 as 18:00).");
            return;
        }

        try {
            // procura profissional disponivel
            Profissional profissional = servico.buscarProfissionalDisponivel(esp, diaSemana, data, horario);
            servico.adicionarConsulta(new Consulta(cpf, paciente.getNome(), profissional.getNome(), data, horario));
            System.out.println("Consulta agendada com " + profissional.getNome() + "!");
        } catch (HorarioIndisponivelException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void cancelarConsulta() {
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        String data = lerData("Data (DD/MM/AAAA): ");
        String horario = lerHorario("Horario (HH:MM): ");

        try {
            // localiza a consulta
            int idx = servico.buscarIndiceConsulta(cpf, data, horario);

            // calculo da multa
            String horaAtual = lerHorario("Horario atual (HH:MM): ");

            int hConsulta = Integer.parseInt(horario.substring(0, 2));
            int hAgora = Integer.parseInt(horaAtual.substring(0, 2));
            int diff = hConsulta - hAgora;

            if (diff < 2) {
                System.out.println("Multa de R$50.00 aplicada!");
                servico.registrarMulta(50.0);
            }

            int temMotivo = lerInteiro("Informar motivo? (1-Nao / 2-Sim): ");

            if (temMotivo == 1) {
                servico.cancelarConsulta(idx);
                System.out.println("Consulta cancelada.");
            } else {
                System.out.print("Motivo: ");
                String motivo = sc.nextLine();
                servico.cancelarConsulta(idx);
                System.out.println("Consulta cancelada. Motivo: " + motivo);
            }

        } catch (ConsultaNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (OperacaoInvalidaException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void remarcarConsulta() {
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        String dataOrig = lerData("Data original (DD/MM/AAAA): ");
        String horarioOrig = lerHorario("Horario original (HH:MM): ");

        try {
            int idx = servico.buscarIndiceConsultaAgendada(cpf, dataOrig, horarioOrig);
            Consulta consultaOriginal = servico.getConsulta(idx);

            int tipo = lerInteiro("Apenas trocar horario no mesmo dia? (1-Sim / 2-Nao): ");

            String novaData;
            String novoHorario;

            if (tipo == 1) {
                novaData = dataOrig;
                novoHorario = lerHorario("Novo horario (HH:MM): ");
            } else {
                novaData = lerData("Nova data (DD/MM/AAAA): ");
                novoHorario = lerHorario("Novo horario (HH:MM): ");
            }

            String nomeProf = consultaOriginal.getNomeProfissional();
            Profissional profissional = servico.buscarProfissional(nomeProf);

            // verifica se o profissional atende no dia e turno do novo horário,
            // independentemente de a data ter mudado ou só o horário
            String diaNovo = (tipo == 1) ? descobrirDiaSemana(dataOrig) : descobrirDiaSemana(novaData);
            String turnoNovo = HorarioDisponivel.turnoDoHorario(novoHorario);

            if (turnoNovo.equals(HorarioDisponivel.FORA_DO_EXPEDIENTE)) {
                throw new HorarioIndisponivelException("Horario fora do expediente da clinica (08:00 as 18:00).");
            }

            if (!profissional.atendeNoDiaETurno(diaNovo, turnoNovo)) {
                throw new HorarioIndisponivelException("Profissional nao atende nesse dia/turno.");
            }

            if (servico.temConflito(nomeProf, novaData, novoHorario)) {
                throw new HorarioIndisponivelException("Horario ocupado. Nao foi possivel remarcar.");
            }

            consultaOriginal.remarcar(novaData, novoHorario);

            Consulta novaConsulta = new Consulta(
                    cpf,
                    consultaOriginal.getNomePaciente(),
                    nomeProf,
                    novaData,
                    novoHorario,
                    consultaOriginal.getTipo());

            servico.adicionarConsulta(novaConsulta);
            System.out.println("Consulta remarcada com sucesso!");

        } catch (ConsultaNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (ProfissionalNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (HorarioIndisponivelException e) {
            System.out.println(e.getMessage());
        } catch (OperacaoInvalidaException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void listarConsultas() {
        List<Consulta> consultas = servico.listarConsultas();

        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta.");
            return;
        }
        for (int i = 0; i < consultas.size(); i++) {
            System.out.println("[" + i + "] " + consultas.get(i).exportarDados());
        }
    }

    public static void buscarConsultasPorPaciente() {
        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        List<Consulta> encontradas = servico.buscarConsultasPorPaciente(cpf);

        if (encontradas.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada.");
            return;
        }

        List<Consulta> todas = servico.listarConsultas();
        for (Consulta c : encontradas) {
            System.out.println("[" + todas.indexOf(c) + "] " + c.exportarDados());
        }
    }

    // descobre dia da semana a partir da data
    // operação opcional
    public static String descobrirDiaSemana(String data) {
        int dia = Integer.parseInt(data.substring(0, 2));
        int mes = Integer.parseInt(data.substring(3, 5));
        int ano = Integer.parseInt(data.substring(6, 10));

        // ajuste pra formula funcionar com janeiro e fevereiro
        if (mes < 3) {
            mes = mes + 12;
            ano = ano - 1;
        }

        int k = ano % 100;
        int j = ano / 100;

        // formula de zeller
        int resultado = (dia + (13 * (mes + 1)) / 5 + k + k/4 + j/4 - 2*j) % 7;
        if (resultado < 0) resultado = resultado + 7;

        // 0=sabado, 1=domingo, 2=segunda...
        String[] nomes = {"sabado", "domingo", "segunda", "terca", "quarta", "quinta", "sexta"};
        return nomes[resultado];
    }

    // ---- ATENDIMENTOS ----

    public static void menuAtendimentos() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- ATENDIMENTOS ---");
            System.out.println("1 - Registrar atendimento");
            System.out.println("0 - Voltar");
            op = lerInteiro("Opcao: ");

            if (op == 1) registrarAtendimento();
        }
    }

    public static void registrarAtendimento() {
        int idxConsulta = lerInteiro("Indice da consulta: ");

        try {
            Consulta consulta = servico.getConsulta(idxConsulta);

            if (!consulta.isAgendada()) {
                throw new OperacaoInvalidaException("So pode registrar atendimento em consulta agendada.");
            }

            System.out.print("Observacoes: ");
            String obs = sc.nextLine();

            int tipo = lerInteiro("Tipo de registro (1-So obs / 2-Com diagnostico / 3-Completo): ");

            Atendimento atendimento;

            if (tipo == 1) {
                atendimento = new Atendimento(consulta, obs);

            } else if (tipo == 2) {
                System.out.print("Diagnostico: ");
                String diag = sc.nextLine();
                atendimento = new Atendimento(consulta, obs, diag);

            } else {
                System.out.print("Diagnostico: ");
                String diag = sc.nextLine();

                int forma = lerInteiro("Como informar procedimentos? (1-Um por vez / 2-Todos de uma vez): ");

                String[] procs = new String[10];
                int qtdProcs = 0;

                if (forma == 1) {
                    String proc = "";
                    while (!proc.equals("fim") && qtdProcs < 10) {
                        System.out.print("Procedimento (ou 'fim'): ");
                        proc = sc.nextLine();
                        if (!proc.equals("fim")) {
                            procs[qtdProcs] = proc;
                            qtdProcs++;
                        }
                    }
                } else {
                    qtdProcs = lerInteiro("Quantos? ");
                    if (qtdProcs > 10) qtdProcs = 10;
                    for (int i = 0; i < qtdProcs; i++) {
                        System.out.print("Proc " + (i+1) + ": ");
                        procs[i] = sc.nextLine();
                    }
                }
                atendimento = new Atendimento(consulta, obs, diag, procs, qtdProcs);
            }

            servico.registrarAtendimento(atendimento);

            // LIGAÇÃO DINÂMICA: o registrarEspecifico() chamado é o da subclasse REAL
            // do profissional (Fisioterapeuta, Psicologo, Nutricionista ou ClinicoGeral),
            // decidido em tempo de execução, não pelo tipo declarado da referência
            Profissional profissional = servico.buscarProfissional(consulta.getNomeProfissional());
            profissional.registrarEspecifico(atendimento);

            consulta.concluir();

            System.out.println("\n--- RESUMO ---");
            System.out.println(servico.getUltimoAtendimento().exibirResumo());
            System.out.println("Consulta marcada como concluida.");

        } catch (ConsultaNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (OperacaoInvalidaException e) {
            System.out.println(e.getMessage());
        } catch (ProfissionalNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    // ---- PAGAMENTOS ----

    public static void menuPagamentos() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- PAGAMENTOS ---");
            System.out.println("1 - Pagamento direto");
            System.out.println("2 - Pagamento automatico");
            System.out.println("3 - Listar pagamentos");
            System.out.println("0 - Voltar");
            op = lerInteiro("Opcao: ");

            switch (op) {
                case 1: pagamentoDireto(); break;
                case 2: pagamentoAutomatico(); break;
                case 3: listarPagamentos(); break;
                case 0: break;
                default: System.out.println("Opcao invalida!"); break;
            }
        }
    }

    public static void pagamentoDireto() {
        int idxConsulta = lerInteiro("Indice da consulta: ");

        try {
            Consulta consulta = servico.getConsulta(idxConsulta);
            double valorBase = lerDouble("Valor base: ");

            Pagamento pagamento = criarPagamento(idxConsulta, valorBase, consulta);

            servico.registrarPagamento(pagamento);
            System.out.println(pagamento.exibirResumo());
            System.out.println("Pagamento registrado!");

        } catch (ConsultaNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (PagamentoInvalidoException e) {
            System.out.println(e.getMessage());
        } catch (ConvenioNaoCobreException e) {
            System.out.println(e.getMessage());
        } catch (PacienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (ProfissionalNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("--- Operacao de pagamento finalizada ---");
        }
    }

    public static void pagamentoAutomatico() {
        int idxConsulta = lerInteiro("Indice da consulta: ");

        try {
            Consulta consulta = servico.getConsulta(idxConsulta);

            // obtem valor base direto da tabela do profissional
            String nomeProf = consulta.getNomeProfissional();
            Profissional profissional = servico.buscarProfissional(nomeProf);
            double valorBase = profissional.getValorConsulta();

            System.out.println("Valor base (tabela do profissional): R$" + valorBase);

            Pagamento pagamento = criarPagamento(idxConsulta, valorBase, consulta);

            servico.registrarPagamento(pagamento);
            System.out.println(pagamento.exibirResumo());
            System.out.println("Pagamento registrado!");

        } catch (ConsultaNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (ProfissionalNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (PagamentoInvalidoException e) {
            System.out.println(e.getMessage());
        } catch (ConvenioNaoCobreException e) {
            System.out.println(e.getMessage());
        } catch (PacienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("--- Operacao de pagamento finalizada ---");
        }
    }

    // Pergunta o tipo de pagamento e instancia a subclasse de Pagamento correta
    // (LIGAÇÃO DINÂMICA: o calcularValorFinal() chamado depende do tipo REAL do
    // objeto criado aqui, não do tipo da referência Pagamento)
    private static Pagamento criarPagamento(int idxConsulta, double valorBase, Consulta consulta)
            throws PagamentoInvalidoException, ConvenioNaoCobreException,
                   PacienteNaoEncontradoException, ProfissionalNaoEncontradoException {

        if (valorBase < 0) {
            throw new PagamentoInvalidoException("Valor base nao pode ser negativo.");
        }

        System.out.print("Tipo (dinheiro/cartao/convenio): ");
        String tipoPag = sc.nextLine();

        switch (tipoPag) {
            case "dinheiro":
                return new PagamentoDinheiro(idxConsulta, valorBase);

            case "cartao":
                int parc = lerInteiro("Parcelas (1 a " + PagamentoCartao.MAX_PARCELAS + "): ");
                if (parc < PagamentoCartao.MIN_PARCELAS || parc > PagamentoCartao.MAX_PARCELAS) {
                    throw new PagamentoInvalidoException(
                            "Parcelas devem estar entre " + PagamentoCartao.MIN_PARCELAS
                                    + " e " + PagamentoCartao.MAX_PARCELAS + ".");
                }
                return new PagamentoCartao(idxConsulta, valorBase, parc);

            case "convenio":
                Paciente paciente = servico.buscarPaciente(consulta.getCpf());
                if (!paciente.temConvenio()) {
                    throw new ConvenioNaoCobreException("Paciente nao possui convenio cadastrado.");
                }
                String especialidade = servico.buscarProfissional(consulta.getNomeProfissional()).getEspecialidade();
                if (!paciente.getConvenio().cobre(especialidade)) {
                    throw new ConvenioNaoCobreException(
                            "Convenio " + paciente.getConvenio().getNome() + " nao cobre " + especialidade + ".");
                }
                return new PagamentoConvenio(idxConsulta, valorBase, paciente.getConvenio());

            default:
                throw new PagamentoInvalidoException("Tipo de pagamento invalido: " + tipoPag);
        }
    }

    public static void listarPagamentos() {
        List<Pagamento> pagamentos = servico.listarPagamentos();

        if (pagamentos.isEmpty()) {
            System.out.println("Nenhum pagamento registrado.");
            return;
        }
        for (Pagamento p : pagamentos) {
            System.out.println(p.exibirResumo());
        }
    }

    // ---- RELATORIOS ----

    public static void menuRelatorios() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- RELATORIOS ---");
            System.out.println("1 - Geral");
            System.out.println("2 - Por profissional");
            System.out.println("3 - Por periodo");
            System.out.println("4 - Resumo financeiro");
            System.out.println("5 - Unificado de cadastros (pacientes + profissionais)");
            System.out.println("0 - Voltar");
            op = lerInteiro("Opcao: ");

            List<Consulta> consultas = servico.listarConsultas();
            List<Atendimento> atendimentos = servico.listarAtendimentos();
            List<Pagamento> pagamentos = servico.listarPagamentos();
            List<Double> multas = servico.listarMultas();

            switch (op) {
                case 1:
                    Relatorio.gerarRelatorio(consultas, atendimentos);
                    break;
                case 2:
                    System.out.print("Nome do profissional: ");
                    String nome = sc.nextLine();
                    Relatorio.gerarRelatorio(consultas, atendimentos, nome);
                    break;
                case 3:
                    String ini = lerData("Data inicio (DD/MM/AAAA): ");
                    String fim = lerData("Data fim (DD/MM/AAAA): ");
                    Relatorio.gerarRelatorio(consultas, atendimentos, ini, fim);
                    break;
                case 4:
                    Relatorio.gerarResumoFinanceiro(consultas, pagamentos, multas);
                    break;
                case 5:
                    List<Pessoa> todasPessoas = servico.listarTodasPessoas();
                    Relatorio.gerarRelatorioUnificado(todasPessoas);
                    break;
                case 0: break;
                default: System.out.println("Opcao invalida!"); break;
            }
        }
    }
}
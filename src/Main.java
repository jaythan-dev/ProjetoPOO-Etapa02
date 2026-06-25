import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static ClinicaServico servico = new ClinicaServico();
    static Scanner sc = new Scanner(System.in);

    private static final String[] DIAS_VALIDOS =
            {"sabado", "domingo", "segunda", "terca", "quarta", "quinta", "sexta"};

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
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(sc.nextLine());

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

            op = Integer.parseInt(sc.nextLine());

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

        System.out.print("Tipo (1-Minimo / 2-Completo): ");
        int tipo = Integer.parseInt(sc.nextLine());

        if (tipo >= 2) {
            System.out.print("Idade: ");
            paciente.setIdade(Integer.parseInt(sc.nextLine()));

            System.out.print("Telefone: ");
            paciente.setTelefone(sc.nextLine());
        }

        if (tipo == 3) {
            System.out.print("Convenio: ");
            paciente.setConvenio(sc.nextLine());
        }

        if (servico.cadastrarPaciente(paciente)) {
            System.out.println("Paciente cadastrado!");
        } else {
            System.out.println("CPF já cadastrado!");
        }
    }

    public static void complementarPaciente() {

        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        Paciente paciente = servico.buscarPaciente(cpf);

        if (paciente == null) {
            System.out.println("Paciente nao encontrado.");
            return;
        }
        System.out.print("Idade: ");
        paciente.setIdade(Integer.parseInt(sc.nextLine()));

        System.out.print("Telefone: ");
        paciente.setTelefone(sc.nextLine());

        System.out.print("Convenio? (2-Sim / 1-Nao): ");
        int tipo = Integer.parseInt(sc.nextLine());

        if (tipo == 2) {
            System.out.print("Convenio: ");
            paciente.setConvenio(sc.nextLine());
        }

        System.out.println("Cadastro atualizado!");
    }

    public static void buscarPaciente() {

        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        Paciente paciente = servico.buscarPaciente(cpf);

        if (paciente == null) {
            System.out.println("Paciente nao encontrado.");
        } else {
            System.out.println(paciente.exibirResumo());
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

        if (servico.desativarPaciente(cpf)) {
            System.out.println("Paciente desativado.");
        } else {
            System.out.println("Paciente nao encontrado.");
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

            try {
                op = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida!");
                continue;
            }

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

            System.out.print("Especialidade: ");
            String esp = sc.nextLine();

            if (!Profissional.especialidadeValida(esp)) {
                System.out.println("Especialidade inválida!");
                return;
            }

            if (servico.buscarProfissional(nome) != null) {
                System.out.println("Profissional já cadastrado!");
                return;
            }

            Profissional prof;

            System.out.print("Tipo (1-Básico / 2-Completo): ");
            int tipo = Integer.parseInt(sc.nextLine());

            if (tipo == 1) {
                prof = new Profissional(nome, esp);
            } else {
                System.out.print("Registro: ");
                String reg = sc.nextLine();

                System.out.print("Valor consulta: ");
                double valor = Double.parseDouble(sc.nextLine());

                System.out.print("Informar dias de atendimento agora? (1-Nao / 2-Sim): ");
                int informarDias = Integer.parseInt(sc.nextLine());

                if (informarDias == 2) {
                    List<String> dias = lerDiasDisponiveis();
                    prof = new Profissional(nome, esp, reg, valor, dias);
                } else {
                    prof = new Profissional(nome, esp, reg, valor);
                }
            }

            boolean ok = servico.cadastrarProfissional(prof);

            if (ok) {
                System.out.println("Profissional cadastrado!");
            } else {
                System.out.println("Erro ao cadastrar profissional.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido!");
        } finally {
            System.out.println("--- operação finalizada ---");
        }
    }

    // =========================
    // ATUALIZAR PROFISSIONAL
    // =========================

    public static void atualizarProfissional() {

        try {
            System.out.print("Nome do profissional: ");
            String nome = sc.nextLine();

            Profissional prof = servico.buscarProfissional(nome);

            if (prof == null) {
                System.out.println("Profissional não encontrado.");
                return;
            }

            System.out.print("Registro: ");
            String reg = sc.nextLine();

            System.out.print("Valor consulta: ");
            double valor = Double.parseDouble(sc.nextLine());

            System.out.print("Atualizar dias de atendimento tambem? (1-Nao / 2-Sim): ");
            int atualizarDias = Integer.parseInt(sc.nextLine());

            if (atualizarDias == 2) {
                List<String> dias = lerDiasDisponiveis();
                prof.atualizar(reg, valor, dias);
            } else {
                prof.atualizar(reg, valor);
            }

            System.out.println("Profissional atualizado!");

        } catch (NumberFormatException e) {
            System.out.println("Número inválido!");
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
    // LEITURA DOS DIAS DE ATENDIMENTO
    // =========================

    private static boolean diaValido(String dia) {
        for (String d : DIAS_VALIDOS) {
            if (d.equals(dia)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> lerDiasDisponiveis() {

        List<String> dias = new ArrayList<>();
        String dia = "";

        System.out.println("Dias validos: sabado, domingo, segunda, terca, quarta, quinta, sexta");

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

            if (dias.contains(dia)) {
                System.out.println("Esse dia ja foi informado.");
                continue;
            }

            dias.add(dia);
        }

        return dias;
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
            System.out.print("Opcao: ");
            op = Integer.parseInt(sc.nextLine());

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
        Paciente paciente = servico.buscarPaciente(cpf);
        if (paciente == null) {
            System.out.println("Paciente nao encontrado.");
            return;
        }
        if (!paciente.isAtivo()) {
            System.out.println("Paciente inativo. Nao e possivel agendar.");
            return;
        }

        System.out.print("Nome do profissional: ");
        String nomeProf = sc.nextLine();
        Profissional profissional = servico.buscarProfissional(nomeProf);
        if (profissional == null) {
            System.out.println("Profissional nao encontrado.");
            return;
        }
        if (profissional.getValorConsulta() == 0) {
            System.out.println("Profissional sem valor definido. Nao pode agendar.");
            return;
        }

        System.out.print("Data (DD/MM/AAAA): ");
        String data = sc.nextLine();
        System.out.print("Horario (HH:MM): ");
        String horario = sc.nextLine();

        // verifica dia da semana
        String diaSemana = descobrirDiaSemana(data);
        if (!profissional.atendeNoDia(diaSemana)) {
            System.out.println("Profissional nao atende nesse dia.");
            return;
        }

        // verifica conflito
        if (servico.temConflito(nomeProf, data, horario)) {
            System.out.println("Horario ocupado!");
            String sugestao = servico.sugerirHorario(nomeProf, data);
            if (sugestao.isEmpty()) {
                System.out.println("Nenhum horario disponivel nesse dia.");
                return;
            }
            System.out.println("Sugestao: " + sugestao);
            System.out.print("Aceita? (1-Sim / 2-Nao): ");
            int aceita = Integer.parseInt(sc.nextLine());
            if (aceita == 1) {
                horario = sugestao;
            } else {
                return;
            }
        }

        System.out.print("Informar tipo? (1-Nao / 2-Sim): ");
        int infoTipo = Integer.parseInt(sc.nextLine());

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
    }

    public static void agendarPorEspecialidade() {
        System.out.print("CPF do paciente: ");
        String cpf = sc.nextLine();
        Paciente paciente = servico.buscarPaciente(cpf);
        if (paciente == null) {
            System.out.println("Paciente nao encontrado.");
            return;
        }
        if (!paciente.isAtivo()) {
            System.out.println("Paciente inativo. Nao e possivel agendar.");
            return;
        }

        System.out.print("Especialidade: ");
        String esp = sc.nextLine();
        System.out.print("Data (DD/MM/AAAA): ");
        String data = sc.nextLine();
        System.out.print("Horario (HH:MM): ");
        String horario = sc.nextLine();

        String diaSemana = descobrirDiaSemana(data);

        // procura profissional disponivel
        Profissional profissional = servico.buscarProfissionalDisponivel(esp, diaSemana, data, horario);

        if (profissional == null) {
            System.out.println("Nenhum profissional disponivel.");
            return;
        }

        servico.adicionarConsulta(new Consulta(cpf, paciente.getNome(), profissional.getNome(), data, horario));
        System.out.println("Consulta agendada com " + profissional.getNome() + "!");
    }

    public static void cancelarConsulta() {
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Data (DD/MM/AAAA): ");
        String data = sc.nextLine();
        System.out.print("Horario (HH:MM): ");
        String horario = sc.nextLine();

        // localiza a consulta
        int idx = servico.buscarIndiceConsulta(cpf, data, horario);

        if (idx == -1) {
            System.out.println("Consulta nao encontrada.");
            return;
        }

        Consulta consulta = servico.getConsulta(idx);

        if (consulta.isConcluida()) {
            System.out.println("Consulta ja realizada. Nao pode cancelar.");
            return;
        }
        if (consulta.getStatus().equals("cancelada")) {
            System.out.println("Consulta ja cancelada.");
            return;
        }

        // calculo da multa
        System.out.print("Horario atual (HH:MM): ");
        String horaAtual = sc.nextLine();

        int hConsulta = Integer.parseInt(horario.substring(0, 2));
        int hAgora = Integer.parseInt(horaAtual.substring(0, 2));
        int diff = hConsulta - hAgora;

        if (diff < 2) {
            System.out.println("Multa de R$50.00 aplicada!");
            servico.registrarMulta(50.0);
        }

        System.out.print("Informar motivo? (1-Nao / 2-Sim): ");
        int temMotivo = Integer.parseInt(sc.nextLine());

        if (temMotivo == 1) {
            consulta.cancelar();
        } else {
            System.out.print("Motivo: ");
            String motivo = sc.nextLine();
            String msg = consulta.cancelar(motivo);
            System.out.println(msg);
        }
        System.out.println("Consulta cancelada.");
    }

    public static void remarcarConsulta() {
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Data original (DD/MM/AAAA): ");
        String dataOrig = sc.nextLine();
        System.out.print("Horario original (HH:MM): ");
        String horarioOrig = sc.nextLine();

        int idx = servico.buscarIndiceConsultaAgendada(cpf, dataOrig, horarioOrig);

        if (idx == -1) {
            System.out.println("Consulta agendada nao encontrada.");
            return;
        }

        Consulta consultaOriginal = servico.getConsulta(idx);

        System.out.print("Apenas trocar horario no mesmo dia? (1-Sim / 2-Nao): ");
        int tipo = Integer.parseInt(sc.nextLine());

        String novaData;
        String novoHorario;

        if (tipo == 1) {
            novaData = dataOrig;
            System.out.print("Novo horario: ");
            novoHorario = sc.nextLine();
        } else {
            System.out.print("Nova data (DD/MM/AAAA): ");
            novaData = sc.nextLine();
            System.out.print("Novo horario (HH:MM): ");
            novoHorario = sc.nextLine();
        }

        String nomeProf = consultaOriginal.getNomeProfissional();
        Profissional profissional = servico.buscarProfissional(nomeProf);

        // se mudou de dia, verifica se prof atende
        if (tipo == 2) {
            String dia = descobrirDiaSemana(novaData);
            if (!profissional.atendeNoDia(dia)) {
                System.out.println("Profissional nao atende nesse dia.");
                return;
            }
        }

        if (servico.temConflito(nomeProf, novaData, novoHorario)) {
            System.out.println("Horario ocupado. Nao foi possivel remarcar.");
            return;
        }

        try {
            consultaOriginal.remarcar(novaData, novoHorario);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return;
        }

        Consulta novaConsulta = new Consulta(
                cpf,
                consultaOriginal.getNomePaciente(),
                nomeProf,
                novaData,
                novoHorario,
                consultaOriginal.getTipo());

        servico.adicionarConsulta(novaConsulta);
        System.out.println("Consulta remarcada com sucesso!");
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
            System.out.print("Opcao: ");
            op = Integer.parseInt(sc.nextLine());

            if (op == 1) registrarAtendimento();
        }
    }

    public static void registrarAtendimento() {
        System.out.print("Indice da consulta: ");
        int idxConsulta = Integer.parseInt(sc.nextLine());

        Consulta consulta = servico.getConsulta(idxConsulta);

        if (consulta == null) {
            System.out.println("Indice invalido.");
            return;
        }
        if (!consulta.isAgendada()) {
            System.out.println("So pode registrar atendimento em consulta agendada.");
            return;
        }

        System.out.print("Observacoes: ");
        String obs = sc.nextLine();

        System.out.print("Tipo de registro (1-So obs / 2-Com diagnostico / 3-Completo): ");
        int tipo = Integer.parseInt(sc.nextLine());

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

            System.out.print("Como informar procedimentos? (1-Um por vez / 2-Todos de uma vez): ");
            int forma = Integer.parseInt(sc.nextLine());

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
                System.out.print("Quantos? ");
                qtdProcs = Integer.parseInt(sc.nextLine());
                if (qtdProcs > 10) qtdProcs = 10;
                for (int i = 0; i < qtdProcs; i++) {
                    System.out.print("Proc " + (i+1) + ": ");
                    procs[i] = sc.nextLine();
                }
            }
            atendimento = new Atendimento(consulta, obs, diag, procs);
        }

        servico.registrarAtendimento(atendimento);
        consulta.concluir();

        System.out.println("\n--- RESUMO ---");
        System.out.println(servico.getUltimoAtendimento().exibirResumo());
        System.out.println("Consulta marcada como concluida.");
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
            System.out.print("Opcao: ");
            op = Integer.parseInt(sc.nextLine());

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
        System.out.print("Indice da consulta: ");
        int idxConsulta = Integer.parseInt(sc.nextLine());

        if (servico.getConsulta(idxConsulta) == null) {
            System.out.println("Indice invalido.");
            return;
        }

        System.out.print("Valor: ");
        double valor = Double.parseDouble(sc.nextLine());
        System.out.print("Tipo (dinheiro/cartao/convenio): ");
        String tipoPag = sc.nextLine();

        Pagamento pagamento;

        if (tipoPag.equals("cartao")) {
            System.out.print("Parcelas (1 a 3): ");
            int parc = Integer.parseInt(sc.nextLine());
            if (parc < 1) parc = 1;
            if (parc > 3) parc = 3;
            pagamento = new Pagamento(idxConsulta, valor, tipoPag, parc);
            if (parc > 1) {
                double vlrParc = Math.round((valor / parc) * 100.0) / 100.0;
                System.out.println("Pagamento em " + parc + "x de R$" + vlrParc);
            }
        } else {
            pagamento = new Pagamento(idxConsulta, valor, tipoPag);
        }

        servico.registrarPagamento(pagamento);
        System.out.println("Pagamento registrado!");
    }

    public static void pagamentoAutomatico() {
        System.out.print("Indice da consulta: ");
        int idxConsulta = Integer.parseInt(sc.nextLine());

        Consulta consulta = servico.getConsulta(idxConsulta);

        if (consulta == null) {
            System.out.println("Indice invalido.");
            return;
        }

        // obtem valor do profissional
        String nomeProf = consulta.getNomeProfissional();
        Profissional profissional = servico.buscarProfissional(nomeProf);
        double valorBase = profissional.getValorConsulta();

        // verifica convenio e tipo
        String cpfPac = consulta.getCpf();
        Paciente paciente = servico.buscarPaciente(cpfPac);

        boolean temConvenio = !paciente.getConvenioNome().isEmpty();
        boolean ehRetorno = "retorno".equals(consulta.getTipo());

        double desconto = 0;
        if (ehRetorno) desconto = desconto + 20;
        if (temConvenio) desconto = desconto + 40;

        System.out.print("Tem multa pendente? (1-Nao / 2-Sim): ");
        int temMulta = Integer.parseInt(sc.nextLine());
        double valorMulta = 0;

        double valorFinal;
        if (temMulta == 1 && desconto == 0) {
            valorFinal = Pagamento.calcularValor(valorBase);
        } else if (temMulta == 1) {
            valorFinal = Pagamento.calcularValor(valorBase, desconto);
        } else {
            System.out.print("Valor da multa: ");
            valorMulta = Double.parseDouble(sc.nextLine());
            valorFinal = Pagamento.calcularValor(valorBase, desconto, valorMulta);
        }

        // mostra detalhes
        System.out.println("Valor base: R$" + valorBase);
        System.out.println("Desconto: " + desconto + "%");
        if (valorMulta > 0) System.out.println("Multa: R$" + valorMulta);
        double vlrFinalArredondado = Math.round(valorFinal * 100.0) / 100.0;
        System.out.println("Valor final: R$" + vlrFinalArredondado);

        System.out.print("Tipo (dinheiro/cartao/convenio): ");
        String tipoPag = sc.nextLine();

        Pagamento pagamento;

        if (tipoPag.equals("cartao")) {
            System.out.print("Parcelas (1 a 3): ");
            int parc = Integer.parseInt(sc.nextLine());
            if (parc < 1) parc = 1;
            if (parc > 3) parc = 3;
            pagamento = new Pagamento(idxConsulta, valorFinal, tipoPag, parc);
            double vlrParc = Math.round((valorFinal / parc) * 100.0) / 100.0;
            System.out.println("Pagamento em " + parc + "x de R$" + vlrParc);
        } else {
            pagamento = new Pagamento(idxConsulta, valorFinal, tipoPag);
        }

        servico.registrarPagamento(pagamento);
        System.out.println("Pagamento registrado!");
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
            System.out.println("0 - Voltar");
            System.out.print("Opcao: ");
            op = Integer.parseInt(sc.nextLine());

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
                    System.out.print("Data inicio (DD/MM/AAAA): ");
                    String ini = sc.nextLine();
                    System.out.print("Data fim (DD/MM/AAAA): ");
                    String fim = sc.nextLine();
                    Relatorio.gerarRelatorio(consultas, atendimentos, ini, fim);
                    break;
                case 4:
                    Relatorio.gerarResumoFinanceiro(consultas, pagamentos, multas);
                    break;
                case 0: break;
                default: System.out.println("Opcao invalida!"); break;
            }
        }
    }
}
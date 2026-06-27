import java.util.List;

public class Relatorio {

    // mostra todas as consultas
    public static void gerarRelatorio(List<Consulta> consultas,
                                       List<Atendimento> atendimentos) {

        System.out.println("\n=== RELATORIO GERAL ===");

        for (Consulta consulta : consultas) {
            imprimirConsultaComDiagnostico(consulta, atendimentos);
        }
    }

    // filtra por profissional
    public static void gerarRelatorio(List<Consulta> consultas,
                                       List<Atendimento> atendimentos,
                                       String nomeProfissional) {

        System.out.println("\n=== RELATORIO - " + nomeProfissional + " ===");
        boolean achou = false;

        for (Consulta consulta : consultas) {
            if (consulta.getNomeProfissional().equals(nomeProfissional)) {
                imprimirConsultaComDiagnostico(consulta, atendimentos);
                achou = true;
            }
        }

        if (!achou) {
            System.out.println("Nenhuma consulta encontrada para esse profissional.");
        }
    }

    // filtra por periodo (data inicio e fim)
    public static void gerarRelatorio(List<Consulta> consultas,
                                       List<Atendimento> atendimentos,
                                       String dataInicio, String dataFim) {

        System.out.println("\n=== RELATORIO - " + dataInicio + " a " + dataFim + " ===");

        for (Consulta consulta : consultas) {
            if (estaNoIntervalo(consulta.getData(), dataInicio, dataFim)) {
                imprimirConsultaComDiagnostico(consulta, atendimentos);
            }
        }
    }

    // resumo financeiro do dia
    public static void gerarResumoFinanceiro(List<Consulta> consultas,
                                              List<Pagamento> pagamentos,
                                              List<Double> multas) {

        int realizadas = 0;
        int canceladas = 0;
        double totalFaturado = 0;
        double totalEmMultas = 0;

        for (Consulta consulta : consultas) {
            if (consulta.isConcluida()) realizadas++;
            if ("cancelada".equals(consulta.getStatus())) canceladas++;
        }

        for (Pagamento pagamento : pagamentos) {
            totalFaturado = totalFaturado + pagamento.calcularValorFinal();
        }

        for (double multa : multas) {
            totalEmMultas = totalEmMultas + multa;
        }

        System.out.println("\n=== RESUMO FINANCEIRO ===");
        System.out.println("Atendimentos realizados: " + realizadas);
        System.out.println("Total faturado: R$" + Math.round(totalFaturado * 100.0) / 100.0);
        System.out.println("Cancelamentos: " + canceladas);
        System.out.println("Total em multas: R$" + Math.round(totalEmMultas * 100.0) / 100.0);
    }

    // imprime a consulta e, se houver, o diagnostico do atendimento ligado a ela
    private static void imprimirConsultaComDiagnostico(Consulta consulta, List<Atendimento> atendimentos) {

        System.out.println(consulta.exportarDados());

        String diag = buscarDiagnostico(consulta, atendimentos);

        if (diag != null && !diag.isBlank()) {
            System.out.println("  Diagnostico: " + diag);
        }

        System.out.println("---");
    }

    // busca o diagnostico do atendimento ligado a essa consulta
    public static String buscarDiagnostico(Consulta consulta, List<Atendimento> atendimentos) {

        for (Atendimento atendimento : atendimentos) {
            if (atendimento.getConsulta() == consulta) {
                return atendimento.getDiagnostico();
            }
        }

        return null;
    }

    // compara datas convertendo pra numero inteiro (AAAAMMDD)
    public static boolean estaNoIntervalo(String data, String inicio, String fim) {
        int valorData = converterDataParaNumero(data);
        int valorInicio = converterDataParaNumero(inicio);
        int valorFim = converterDataParaNumero(fim);
        return valorData >= valorInicio && valorData <= valorFim;
    }

    // converte DD/MM/AAAA pra um numero tipo 20260519 pra poder comparar
    private static int converterDataParaNumero(String data) {
        int dia = Integer.parseInt(data.substring(0, 2));
        int mes = Integer.parseInt(data.substring(3, 5));
        int ano = Integer.parseInt(data.substring(6, 10));
        return ano * 10000 + mes * 100 + dia;
    }
}
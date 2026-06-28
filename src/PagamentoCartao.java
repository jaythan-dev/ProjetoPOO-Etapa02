public class PagamentoCartao extends Pagamento {

    public static final int MAX_PARCELAS = 6;
    public static final int MIN_PARCELAS = 1;
    private static final int PARCELAS_SEM_TAXA = 3;
    private static final double TAXA_POR_PARCELA_EXTRA = 2.5;

    private int parcelas;

    public PagamentoCartao(int indiceConsulta, double valorBase, int parcelas) {
        super(indiceConsulta, valorBase);
        this.parcelas = parcelas;
    }

    public int getParcelas() {
        return parcelas;
    }

    public double getValorParcela() {
        return Math.round((calcularValorFinal() / parcelas) * 100.0) / 100.0;
    }

    @Override
    public double calcularValorFinal() {
        int parcelasExtras = Math.max(0, parcelas - PARCELAS_SEM_TAXA);
        double percentualTaxa = parcelasExtras * TAXA_POR_PARCELA_EXTRA;
        double taxa = getValorBase() * percentualTaxa / 100;
        return getValorBase() + taxa;
    }

    @Override
    protected String getTipoDescricao() {
        return "cartao (" + parcelas + "x)";
    }

    @Override
    public String exibirResumo() {
        String resumo = super.exibirResumo();
        if (parcelas > 1) {
            resumo = resumo + " (R$" + getValorParcela() + " cada)";
        }
        return resumo;
    }
}
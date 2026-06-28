public class PagamentoConvenio extends Pagamento {

    private Convenio convenio;

    public PagamentoConvenio(int indiceConsulta, double valorBase, Convenio convenio) {
        super(indiceConsulta, valorBase);
        this.convenio = convenio;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    @Override
    public double calcularValorFinal() {
        double cobertura = getValorBase() * convenio.getPercentualCobertura() / 100;
        double valor = getValorBase() - cobertura;
        return Math.max(valor, 0);
    }

    @Override
    protected String getTipoDescricao() {
        return "convenio (" + convenio.getNome() + ")";
    }
}
public class PagamentoDinheiro extends Pagamento {

    private static final double PERCENTUAL_DESCONTO = 5;

    public PagamentoDinheiro(int indiceConsulta, double valorBase) {
        super(indiceConsulta, valorBase);
    }

    // SOBRESCRITA: mesmo nome/parâmetros do método abstrato de Pagamento,
    // a classe filha define o cálculo real (resolvido em tempo de execução)
    @Override
    public double calcularValorFinal() {
        double desconto = getValorBase() * PERCENTUAL_DESCONTO / 100;
        return getValorBase() - desconto;
    }

    @Override
    protected String getTipoDescricao() {
        return "dinheiro/pix";
    }
}
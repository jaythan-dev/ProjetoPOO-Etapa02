public abstract class Pagamento implements Exportavel {

    private int indiceConsulta;
    private double valorBase;

    protected Pagamento(int indiceConsulta, double valorBase) {
        this.indiceConsulta = indiceConsulta;
        this.valorBase = valorBase;
    }

    public int getIndiceConsulta() {
        return indiceConsulta;
    }

    public double getValorBase() {
        return valorBase;
    }

    /**
     * Cada forma de pagamento calcula o valor final de um jeito diferente
     * (desconto fixo, taxa de parcelamento, cobertura de convênio).
     */
    public abstract double calcularValorFinal();

    // Método concreto comum: todas as subclasses usam o mesmo formato de exibição,
    // só o cálculo do valor final é que muda (polimorfismo).
    public String exibirResumo() {
        double valorArredondado = Math.round(calcularValorFinal() * 100.0) / 100.0;
        return "Consulta #" + indiceConsulta
                + " | Valor: R$" + valorArredondado
                + " | Tipo: " + getTipoDescricao();
<<<<<<< HEAD
=======
    }

    protected abstract String getTipoDescricao();

    @Override
    public String exportarDados() {
        double valorArredondado = Math.round(calcularValorFinal() * 100.0) / 100.0;
        return "Pagamento{" +
                "indiceConsulta=" + indiceConsulta +
                ", valorBase=" + valorBase +
                ", valorFinal=" + valorArredondado +
                ", tipo='" + getTipoDescricao() + '\'' +
                '}';
>>>>>>> 206b5a265d560a31c7494c378367c79c7a23af23
    }

    protected abstract String getTipoDescricao();

    @Override
    public String exportarDados() {
        double valorArredondado = Math.round(calcularValorFinal() * 100.0) / 100.0;
        return "Pagamento{" +
                "indiceConsulta=" + indiceConsulta +
                ", valorBase=" + valorBase +
                ", valorFinal=" + valorArredondado +
                ", tipo='" + getTipoDescricao() + '\'' +
                '}';
    }
}
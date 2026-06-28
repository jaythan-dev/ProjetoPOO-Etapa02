public class Fisioterapeuta extends Profissional {

    private int totalSessoesPrevistas;

    public Fisioterapeuta(String nome, String cpf) {
        super(nome, cpf, "fisioterapia");
        this.totalSessoesPrevistas = 0;
    }

    public Fisioterapeuta(String nome, String cpf, String registroProfissional,
                          double valorConsulta, int totalSessoesPrevistas) {
        super(nome, cpf, "fisioterapia", registroProfissional, valorConsulta);
        this.totalSessoesPrevistas = totalSessoesPrevistas;
    }

    public int getTotalSessoesPrevistas() {
        return totalSessoesPrevistas;
    }

    public void setTotalSessoesPrevistas(int totalSessoesPrevistas) {
        this.totalSessoesPrevistas = totalSessoesPrevistas;
    }

    // SOBRESCRITA: mesmo nome e parâmetros que o método abstrato de Profissional,
    // a classe filha redefine o comportamento (resolvido em tempo de execução)
    @Override
    public void registrarEspecifico(Atendimento atendimento) {
        atendimento.getProntuario().adicionarInformacaoEspecifica(
                "Sessoes previstas no plano de tratamento: " + totalSessoesPrevistas);
    }

    @Override
    public String exibirResumo() {
        return super.exibirResumo() + " | Sessoes previstas: " + totalSessoesPrevistas;
    }
}
public class ClinicoGeral extends Profissional {

    private String encaminhamento;

    public ClinicoGeral(String nome, String cpf) {
        super(nome, cpf, "clinica geral");
        this.encaminhamento = "";
    }

    public ClinicoGeral(String nome, String cpf, String registroProfissional,
                        double valorConsulta, String encaminhamento) {
        super(nome, cpf, "clinica geral", registroProfissional, valorConsulta);
        this.encaminhamento = encaminhamento;
    }

    public String getEncaminhamento() {
        return encaminhamento;
    }

    public void setEncaminhamento(String encaminhamento) {
        this.encaminhamento = encaminhamento;
    }

    @Override
    public void registrarEspecifico(Atendimento atendimento) {
        atendimento.getProntuario().adicionarInformacaoEspecifica(
                "Encaminhamento: " + encaminhamento);
    }

    @Override
    public String exibirResumo() {
        return super.exibirResumo() + " | Encaminhamento: " + encaminhamento;
    }
}

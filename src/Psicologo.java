public class Psicologo extends Profissional {

    private String abordagem;

    public Psicologo(String nome, String cpf) {
        super(nome, cpf, "psicologia");
        this.abordagem = "";
    }

    public Psicologo(String nome, String cpf, String registroProfissional,
                     double valorConsulta, String abordagem) {
        super(nome, cpf, "psicologia", registroProfissional, valorConsulta);
        this.abordagem = abordagem;
    }

    public String getAbordagem() {
        return abordagem;
    }

    public void setAbordagem(String abordagem) {
        this.abordagem = abordagem;
    }

    @Override
    public void registrarEspecifico(Atendimento atendimento) {
        atendimento.getProntuario().adicionarInformacaoEspecifica(
                "Abordagem terapeutica: " + abordagem);q1q
    }

    @Override
    public String exibirResumo() {
        return super.exibirResumo() + " | Abordagem: " + abordagem;
    }
}
public class Nutricionista extends Profissional {

    private String planoAlimentar;

    public Nutricionista(String nome, String cpf) {
        super(nome, cpf, "nutricao");
        this.planoAlimentar = "";
    }

    public Nutricionista(String nome, String cpf, String registroProfissional,
                         double valorConsulta, String planoAlimentar) {
        super(nome, cpf, "nutricao", registroProfissional, valorConsulta);
        this.planoAlimentar = planoAlimentar;
    }

    public String getPlanoAlimentar() {
        return planoAlimentar;
    }

    public void setPlanoAlimentar(String planoAlimentar) {
        this.planoAlimentar = planoAlimentar;
    }

    @Override
    public void registrarEspecifico(Atendimento atendimento) {
        atendimento.getProntuario().adicionarInformacaoEspecifica(
                "Plano alimentar: " + planoAlimentar);
    }

    @Override
    public String exibirResumo() {
        return super.exibirResumo() + " | Plano alimentar: " + planoAlimentar;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 206b5a265d560a31c7494c378367c79c7a23af23

public abstract class Pessoa {
    public String nome;

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public abstract String exibirResumo();
}
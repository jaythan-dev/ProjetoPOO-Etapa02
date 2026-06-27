public class Paciente extends Pessoa {

    private int idade;
    private Convenio convenio; // ASSOCIAÇÃO: Paciente conhece Convenio, mas ambos existem independentemente
    private boolean ativo;

    public Paciente(String nome, String cpf) {
        super(nome, cpf);
        this.idade = 0;
        this.ativo = true;
    }

    public Paciente(String nome, String cpf, int idade, String telefone) {
        super(nome, cpf);
        this.idade = idade;
        setTelefone(telefone);
        this.ativo = true;
    }

    public Paciente(String nome, String cpf, int idade, String telefone, Convenio convenio) {
        super(nome, cpf);
        this.idade = idade;
        setTelefone(telefone);
        this.convenio = convenio;
        this.ativo = true;
    }

    // getters e setters
    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public int getIdade() {
        return idade;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public boolean temConvenio() {
        return convenio != null;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    // regras de negócio
    public void complementar(int idade, String telefone) {
        this.idade = idade;
        setTelefone(telefone);
    }

    public void complementar(int idade, String telefone, Convenio convenio) {
        this.idade = idade;
        setTelefone(telefone);
        this.convenio = convenio;
    }

    public void desativar() {
        this.ativo = false;
    }

    @Override
    public String exibirResumo() {
        String status = ativo ? "Sim" : "Nao";
        String nomeConvenio = temConvenio() ? convenio.getNome() : "Nenhum";

        return "Nome: " + getNome()
                + " | CPF: " + getCpf()
                + " | Idade: " + idade
                + " | Tel: " + getTelefone()
                + " | Convenio: " + nomeConvenio
                + " | Ativo: " + status;
    }
}
public class Paciente extends Pessoa {
    private String cpf;
    private int idade;
    private String telefone;
    private String convenioNome;
    private boolean ativo;

    public Paciente(String nome, String cpf) {
        super(nome);
        this.cpf = cpf;
        this.idade = 0;
        this.telefone = "";
        this.convenioNome = "";
        this.ativo = true;
    }

    public Paciente(String nome, String cpf, int idade, String telefone) {
        super(nome);
        this.cpf = cpf;
        this.idade = idade;
        this.telefone = telefone;
        this.convenioNome = "";
        this.ativo = true;
    }

    public Paciente(String nome, String cpf, int idade, String telefone, String convenioNome) {
        super(nome);
        this.cpf = cpf;
        this.idade = idade;
        this.telefone = telefone;
        this.convenioNome = convenioNome;
        this.ativo = true;
    }

    // getters e setters
    public String getCpf() {
        return cpf;
    }

    public int getIdade() {
        return idade;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getConvenioNome() {
        return convenioNome;
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
        this.telefone = telefone;
    }

    public void complementar(int idade, String telefone, String convenioNome) {
        this.idade = idade;
        this.telefone = telefone;
        this.convenioNome = convenioNome;
    }

    public void desativar() {
        this.ativo = false;
    }

    @Override
    public String exibirResumo() {
        String status = ativo ? "Sim" : "Nao";

        return "Nome: " + getNome()
                + " | CPF: " + cpf
                + " | Idade: " + idade
                + " | Tel: " + telefone
                + " | Convenio: " + convenioNome
                + " | Ativo: " + status;
    }
}
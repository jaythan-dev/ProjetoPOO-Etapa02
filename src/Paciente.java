public class Paciente extends Pessoa{
    public String cpf;
    public int idade;
    public String telefone;
    public String convenioNome;
    public boolean ativo;

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

    public Paciente(String nome, String cpf, int idade,
                    String telefone, String convenioNome) {
        super(nome);
        this.cpf = cpf;
        this.idade = idade;
        this.telefone = telefone;
        this.convenioNome = convenioNome;
        this.ativo = true;
    }

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
        String status = "Sim";

        if (!ativo) {
            status = "Nao";
        }

        return "Nome: " + nome
                + " | CPF: " + cpf
                + " | Idade: " + idade
                + " | Tel: " + telefone
                + " | Convenio: " + convenioNome
                + " | Ativo: " + status;
    }
}

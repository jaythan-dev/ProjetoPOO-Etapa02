import java.util.ArrayList;
import java.util.List;

public class Profissional extends Pessoa {

    private String especialidade;
    private String registroProfissional;
    private double valorConsulta;
    private List<String> diasDisponiveis;

    public Profissional(String nome, String especialidade) {
        super(nome);
        this.especialidade = especialidade;
        this.registroProfissional = "";
        this.valorConsulta = 0;
        this.diasDisponiveis = new ArrayList<>();
    }

    public Profissional(String nome, String especialidade,
                        String registroProfissional,
                        double valorConsulta) {
        super(nome);
        this.especialidade = especialidade;
        this.registroProfissional = registroProfissional;
        this.valorConsulta = valorConsulta;
        this.diasDisponiveis = new ArrayList<>();
    }

    public Profissional(String nome, String especialidade,
                        String registroProfissional,
                        double valorConsulta,
                        List<String> dias) {
        super(nome);
        this.especialidade = especialidade;
        this.registroProfissional = registroProfissional;
        this.valorConsulta = valorConsulta;
        this.diasDisponiveis = new ArrayList<>(dias);
    }

    // getters
    public String getEspecialidade() {
        return especialidade;
    }

    public String getRegistroProfissional() {
        return registroProfissional;
    }

    public double getValorConsulta() {
        return valorConsulta;
    }

    public int getTotalDias() {
        return diasDisponiveis.size();
    }

    // setters controlados
    public void atualizar(String registro, double valor) {
        this.registroProfissional = registro;
        this.valorConsulta = valor;
    }

    public void atualizar(String registro, double valor, List<String> dias) {
        this.registroProfissional = registro;
        this.valorConsulta = valor;
        diasDisponiveis.clear();
        diasDisponiveis.addAll(dias);
    }

    public boolean atendeNoDia(String dia) {
        for (String diaDisponivel : diasDisponiveis) {
            if (diaDisponivel.equals(dia)) {
                return true;
            }
        }
        return false;
    }

    public static boolean especialidadeValida(String esp) {
        return esp.equals("clinica geral")
                || esp.equals("fisioterapia")
                || esp.equals("psicologia")
                || esp.equals("nutricao");
    }

    @Override
    public String exibirResumo() {
        String dias = "";

        for (int i = 0; i < diasDisponiveis.size(); i++) {
            if (i > 0) dias += ", ";
            dias += diasDisponiveis.get(i);
        }

        return "Nome: " + getNome()
                + " | Espec: " + especialidade
                + " | Reg: " + registroProfissional
                + " | Valor: R$" + valorConsulta
                + " | Dias: " + dias;
    }
}

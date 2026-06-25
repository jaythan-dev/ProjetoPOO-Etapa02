import java.util.ArrayList;

public class Profissional extends Pessoa {

    private String especialidade;
    private String registroProfissional;
    private double valorConsulta;
    private ArrayList<String> diasDisponiveis;
    private int totalDias;

    public Profissional(String nome, String especialidade) {
        super(nome);
        this.especialidade = especialidade;
        this.registroProfissional = "";
        this.valorConsulta = 0;
        this.diasDisponiveis = new ArrayList<>();
        this.totalDias = 0;
    }

    public Profissional(String nome, String especialidade,
                        String registroProfissional,
                        double valorConsulta) {
        super(nome);
        this.especialidade = especialidade;
        this.registroProfissional = registroProfissional;
        this.valorConsulta = valorConsulta;
        this.diasDisponiveis = new ArrayList<>();
        this.totalDias = 0;
    }

    public Profissional(String nome, String especialidade,
                        String registroProfissional,
                        double valorConsulta,
                        String[] dias,
                        int totalDias) {
        super(nome);
        this.especialidade = especialidade;
        this.registroProfissional = registroProfissional;
        this.valorConsulta = valorConsulta;
        this.diasDisponiveis = new ArrayList<>(); 
        this.totalDias = totalDias;

        for (int i = 0; i < totalDias; i++) {
            this.diasDisponiveis.add(dias[i]);
        }
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
        return totalDias;
    }

    // setters controlados
    public void atualizar(String registro, double valor) {
        this.registroProfissional = registro;
        this.valorConsulta = valor;
    }

    public void atualizar(String registro, double valor,
                          String[] dias, int totalDias) {
        this.registroProfissional = registro;
        this.valorConsulta = valor;
        this.totalDias = totalDias;
        diasDisponiveis.clear();
        for (int i = 0; i < totalDias; i++) {
            diasDisponiveis.add(dias[i]);
        }
    }

    public boolean atendeNoDia(String dia) {
        for (int i = 0; i < totalDias; i++) {
            if (diasDisponiveis.get(i).equals(dia)) {
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

        for (int i = 0; i < totalDias; i++) {
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

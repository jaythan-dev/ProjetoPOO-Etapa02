public class Profissional extends Pessoa {
    public String especialidade;
    public String registroProfissional;
    public double valorConsulta;
    public String[] diasDisponiveis;
    public int totalDias;

    public Profissional(String nome, String especialidade) {
        super(nome);
        this.especialidade = especialidade;
        this.registroProfissional = "";
        this.valorConsulta = 0;
        this.diasDisponiveis = new String[7];
        this.totalDias = 0;
    }

    public Profissional(String nome, String especialidade,
                        String registroProfissional,
                        double valorConsulta) {
        super(nome);
        this.especialidade = especialidade;
        this.registroProfissional = registroProfissional;
        this.valorConsulta = valorConsulta;
        this.diasDisponiveis = new String[7];
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
        this.diasDisponiveis = new String[7];
        this.totalDias = totalDias;

        for (int i = 0; i < totalDias; i++) {
            this.diasDisponiveis[i] = dias[i];
        }
    }

    public void atualizar(String registro, double valor) {
        this.registroProfissional = registro;
        this.valorConsulta = valor;
    }

    public void atualizar(String registro, double valor,
                          String[] dias, int totalDias) {
        this.registroProfissional = registro;
        this.valorConsulta = valor;
        this.totalDias = totalDias;

        for (int i = 0; i < totalDias; i++) {
            this.diasDisponiveis[i] = dias[i];
        }
    }

    public boolean atendeNoDia(String dia) {
        for (int i = 0; i < totalDias; i++) {
            if (diasDisponiveis[i].equals(dia)) {
                return true;
            }
        }
        return false;
    }

    public static boolean especialidadeValida(String esp) {
        if (esp.equals("clinica geral")) return true;
        if (esp.equals("fisioterapia")) return true;
        if (esp.equals("psicologia")) return true;
        if (esp.equals("nutricao")) return true;
        return false;
    }

    @Override
    public String exibirResumo() {
        String dias = "";

        for (int i = 0; i < totalDias; i++) {
            if (i > 0) {
                dias += ", ";
            }

            dias += diasDisponiveis[i];
        }

        return "Nome: " + nome
                + " | Espec: " + especialidade
                + " | Reg: " + registroProfissional
                + " | Valor: R$" + valorConsulta
                + " | Dias: " + dias;
    }
}
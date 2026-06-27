import java.util.ArrayList;
import java.util.List;

public abstract class Profissional extends Pessoa {

    private String especialidade;
    private String registroProfissional;
    private double valorConsulta;

    // AGREGAÇÃO: Profissional possui horários, mas os horários sobrevivem
    // independentemente do profissional (podem ser reaproveitados por outro)
    private List<HorarioDisponivel> horariosDisponiveis;

    protected Profissional(String nome, String cpf, String especialidade) {
        super(nome, cpf);
        this.especialidade = especialidade;
        this.registroProfissional = "";
        this.valorConsulta = 0;
        this.horariosDisponiveis = new ArrayList<>();
    }

    protected Profissional(String nome, String cpf, String especialidade,
                        String registroProfissional,
                        double valorConsulta) {
        super(nome, cpf);
        this.especialidade = especialidade;
        this.registroProfissional = registroProfissional;
        this.valorConsulta = valorConsulta;
        this.horariosDisponiveis = new ArrayList<>();
    }

    protected Profissional(String nome, String cpf, String especialidade,
                        String registroProfissional,
                        double valorConsulta,
                        List<HorarioDisponivel> horarios) {
        super(nome, cpf);
        this.especialidade = especialidade;
        this.registroProfissional = registroProfissional;
        this.valorConsulta = valorConsulta;
        this.horariosDisponiveis = new ArrayList<>(horarios);
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
        return horariosDisponiveis.size();
    }

    public List<HorarioDisponivel> getHorariosDisponiveis() {
        return horariosDisponiveis;
    }

    // setters controlados
    public void atualizar(String registro, double valor) {
        this.registroProfissional = registro;
        this.valorConsulta = valor;
    }

    public void atualizar(String registro, double valor, List<HorarioDisponivel> horarios) {
        this.registroProfissional = registro;
        this.valorConsulta = valor;
        horariosDisponiveis.clear();
        horariosDisponiveis.addAll(horarios);
    }

    /**
     * Verifica se o profissional atende no dia E no turno informados
     * (ex: atende segunda, mas só de manhã — não deve aceitar agendamento
     * de segunda à tarde).
     */
    public boolean atendeNoDiaETurno(String dia, String turno) {
        for (HorarioDisponivel h : horariosDisponiveis) {
            if (h.getDiaSemana().equals(dia) && h.getTurno().equals(turno)) {
                return true;
            }
        }
        return false;
    }

    // Método protegido: cálculo auxiliar interno, só as subclasses precisam dele
    // (modificador de acesso com propósito: nenhuma classe externa usa isso)
    protected String formatarHorarios() {
        String dias = "";
        for (int i = 0; i < horariosDisponiveis.size(); i++) {
            if (i > 0) dias += ", ";
            dias += horariosDisponiveis.get(i);
        }
        return dias;
    }

    /**
     * Cada especialidade registra, à sua maneira, as informações específicas
     * do atendimento (ex: sessões previstas, abordagem, plano alimentar,
     * encaminhamento) no prontuário do atendimento informado.
     */
    public abstract void registrarEspecifico(Atendimento atendimento);

    @Override
    public String exibirResumo() {
        return "Nome: " + getNome()
                + " | CPF: " + getCpf()
                + " | Espec: " + especialidade
                + " | Reg: " + registroProfissional
                + " | Valor: R$" + valorConsulta
                + " | Dias: " + formatarHorarios();
    }
}
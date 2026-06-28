import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Atendimento implements Exportavel {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Consulta consulta;
    private Prontuario prontuario; // COMPOSIÇÃO: o prontuário nasce e morre com o atendimento

    // registro basico - so observacoes
    public Atendimento(Consulta consulta, String observacoes) {
        this.consulta = consulta;
        this.prontuario = new Prontuario(observacoes, "", dataDeHoje());
    }

    public Atendimento(Consulta consulta, String observacoes, String diagnostico) {
        this.consulta = consulta;
        this.prontuario = new Prontuario(observacoes, diagnostico, dataDeHoje());
    }

    // registro completo com procedimentos ja definidos
    public Atendimento(Consulta consulta, String observacoes, String diagnostico,
                       String[] procedimentos, int quantidade) {
        this.consulta = consulta;
        this.prontuario = new Prontuario(observacoes, diagnostico, dataDeHoje());
        this.prontuario.adicionarProcedimentos(procedimentos, quantidade);
    }

    private static String dataDeHoje() {
        return LocalDate.now().format(FORMATO_DATA);
    }

    // adiciona um por vez
    public void adicionarProcedimento(String procedimento) {
        prontuario.adicionarProcedimento(procedimento);
    }

    // adiciona varios de uma vez
    public void adicionarProcedimento(String[] procs, int quantidade) {
        prontuario.adicionarProcedimentos(procs, quantidade);
    }

    // getters
    public String getObservacoes() {
        return prontuario.getObservacoes();
    }

    public String getDiagnostico() {
        return prontuario.getDiagnostico();
    }

    public List<String> getProcedimentos() {
        return prontuario.getProcedimentos();
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public Prontuario getProntuario() {
        return prontuario;
    }

    public int getTotalProcedimentos() {
        return prontuario.getProcedimentos().size();
    }

    public String exibirResumo() {
        return prontuario.exibirResumo();
    }

    @Override
    public String exportarDados() {
        return "Atendimento{" +
                "consulta='" + consulta.getCpf() + " - " + consulta.getData() + " " + consulta.getHorario() + '\'' +
                ", observacoes='" + getObservacoes() + '\'' +
                ", diagnostico='" + getDiagnostico() + '\'' +
                ", totalProcedimentos=" + getTotalProcedimentos() +
                '}';
    }
}
import java.util.List;

public class Convenio {

    private String nome;
    private double percentualCobertura;
    private List<String> especialidadesCobertas;

    public Convenio(String nome, double percentualCobertura, List<String> especialidadesCobertas) {
        this.nome = nome;
        this.percentualCobertura = percentualCobertura;
        this.especialidadesCobertas = especialidadesCobertas;
    }

    public String getNome() {
        return nome;
    }

    public double getPercentualCobertura() {
        return percentualCobertura;
    }

    public List<String> getEspecialidadesCobertas() {
        return especialidadesCobertas;
    }

    public boolean cobre(String especialidade) {
        return especialidadesCobertas.contains(especialidade);
    }

    public String exibirResumo() {
        return "Convenio: " + nome + " | Cobertura: " + percentualCobertura + "%";
    }

    // ====================
    // CONVENIOS FIXOS DA CLINICA
    // ====================

    private static final List<String> TODAS_ESPECIALIDADES =
            List.of("clinica geral", "fisioterapia", "psicologia", "nutricao");

    public static Convenio saudePlus() {
        return new Convenio("SaudePlus", 40, TODAS_ESPECIALIDADES);
    }

    public static Convenio vidaMais() {
        return new Convenio("VidaMais", 30, TODAS_ESPECIALIDADES);
    }

    public static Convenio bemEstar() {
        return new Convenio("BemEstar", 50, TODAS_ESPECIALIDADES);
    }
}

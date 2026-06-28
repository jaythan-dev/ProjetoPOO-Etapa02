public class HorarioDisponivel {

    private String diaSemana;
    private String turno; // manha, tarde

    public HorarioDisponivel(String diaSemana, String turno) {
        this.diaSemana = diaSemana;
        this.turno = turno;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public String getTurno() {
        return turno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorarioDisponivel)) return false;
        HorarioDisponivel outro = (HorarioDisponivel) o;
        return diaSemana.equals(outro.diaSemana) && turno.equals(outro.turno);
    }

    @Override
    public int hashCode() {
        return diaSemana.hashCode() * 31 + turno.hashCode();
    }

    @Override
    public String toString() {
        return diaSemana + " (" + turno + ")";
    }

    // Faixas fixas da clinica: manha 08:00-11:59, tarde 12:00-18:00
    private static final int INICIO_EXPEDIENTE_MINUTOS = 8 * 60;  // 08:00
    private static final int INICIO_TARDE_MINUTOS = 12 * 60;      // 12:00
    private static final int FIM_EXPEDIENTE_MINUTOS = 18 * 60;    // 18:00

    public static final String MANHA = "manha";
    public static final String TARDE = "tarde";
    public static final String FORA_DO_EXPEDIENTE = "fora_do_expediente";

    /**
     * Deriva o turno (manha/tarde) a partir de um horário no formato HH:MM,
     * ou retorna FORA_DO_EXPEDIENTE se o horário estiver fora de 08:00-18:00.
     */
    public static String turnoDoHorario(String horario) {
        int hora = Integer.parseInt(horario.substring(0, 2));
        int minuto = Integer.parseInt(horario.substring(3, 5));
        int totalMinutos = hora * 60 + minuto;

        if (totalMinutos < INICIO_EXPEDIENTE_MINUTOS || totalMinutos > FIM_EXPEDIENTE_MINUTOS) {
            return FORA_DO_EXPEDIENTE;
        }

        return (totalMinutos < INICIO_TARDE_MINUTOS) ? MANHA : TARDE;
    }
}
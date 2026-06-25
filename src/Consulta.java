public class Consulta implements Agendavel, Exportavel {

    // ====================
    // ATRIBUTOS (ENCAPSULADOS)
    // ====================

    private String cpf;
    private String nomePaciente;
    private String nomeProfissional;
    private String data;
    private String horario;
    private String tipo; // inicial, retorno, avaliacao

    private String status; // agendada, cancelada, remarcada, concluida

    // ====================
    // SOBRECARGA DE CONSTRUTORES
    // ====================

    public Consulta() {
        this.status = "agendada";
    }

    public Consulta(String cpf, String nomePaciente, String nomeProfissional,
                    String data, String horario) {
        this.cpf = cpf;
        this.nomePaciente = nomePaciente;
        this.nomeProfissional = nomeProfissional;
        this.data = data;
        this.horario = horario;
        this.status = "agendada";
    }

    public Consulta(String cpf, String nomePaciente, String nomeProfissional,
                    String data, String horario, String tipo) {
        this(cpf, nomePaciente, nomeProfissional, data, horario);
        this.tipo = tipo;
    }

    // ====================
    // GETTERS / SETTERS (ENCAPSULAMENTO)
    // ====================

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF inválido");
        }
        this.cpf = cpf;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getNomeProfissional() {
        return nomeProfissional;
    }

    public void setNomeProfissional(String nomeProfissional) {
        this.nomeProfissional = nomeProfissional;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }
    // ====================
    // AGENDAVEL (INTERFACE)
    // ====================

    @Override
    public void agendar() {
        this.status = "agendada";
    }

    @Override
    public void cancelar() {
        if ("concluida".equals(status)) {
            throw new IllegalStateException("Consulta já realizada não pode ser cancelada");
        }
        this.status = "cancelada";
    }

    /**
     * Cancela a consulta registrando um motivo e devolve uma mensagem
     * de confirmação pronta para exibição.
     */
    public String cancelar(String motivo) {
        cancelar();
        return "Consulta cancelada. Motivo: " + motivo;
    }

    /**
     * Marca esta consulta como remarcada (registro histórico).
     * A data e o horário originais são preservados; a nova consulta,
     * com a nova data e horário, deve ser criada separadamente
     * (ver ClinicaServico/Main), conforme a jornada de remarcação.
     */
    @Override
    public void remarcar(String novaData, String novoHorario) {

        if ("cancelada".equals(status)) {
            throw new IllegalStateException("Consulta cancelada não pode ser remarcada");
        }
        if ("concluida".equals(status)) {
            throw new IllegalStateException("Consulta já realizada não pode ser remarcada");
        }

        this.status = "remarcada";
    }

    /**
     * Marca a consulta como concluída (atendimento realizado).
     */
    public void concluir() {
        if ("cancelada".equals(status)) {
            throw new IllegalStateException("Consulta cancelada não pode ser concluída");
        }
        this.status = "concluida";
    }

    // ====================
    // EXPORTAVEL
    // ====================

    @Override
    public String exportarDados() {
        return "Consulta{" +
                "cpf='" + cpf + '\'' +
                ", paciente='" + nomePaciente + '\'' +
                ", profissional='" + nomeProfissional + '\'' +
                ", data='" + data + '\'' +
                ", horario='" + horario + '\'' +
                ", tipo='" + tipo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    // ====================
    // LÓGICA AUXILIAR
    // ====================

    public boolean isAgendada() {
        return "agendada".equals(status);
    }

    public boolean isConcluida() {
        return "concluida".equals(status);
    }
}
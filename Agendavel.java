public interface Agendavel {

    void agendar();

    void cancelar() throws OperacaoInvalidaException;

    void remarcar(String novaData, String novoHorario) throws OperacaoInvalidaException;

}
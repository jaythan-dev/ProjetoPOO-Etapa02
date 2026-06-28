public class ConsultaNaoEncontradaException extends Exception {

    public ConsultaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public ConsultaNaoEncontradaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
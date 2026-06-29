public class PagamentoInvalidoException extends Exception {

    public PagamentoInvalidoException(String mensagem) {
        super(mensagem);
    }

    public PagamentoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
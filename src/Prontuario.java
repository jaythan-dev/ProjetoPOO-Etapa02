import java.util.ArrayList;
import java.util.List;

// COMPOSIÇÃO: Prontuario só existe dentro de Atendimento — se o Atendimento for
// removido, o Prontuario também é. Por isso o construtor é package-private:
// só a própria Atendimento (mesmo pacote) pode criar um Prontuario.
public class Prontuario {

    private String observacoes;
    private String diagnostico;
    private List<String> procedimentos;
    private List<String> informacoesEspecificas;
    private String dataRegistro;

    // package-private: ninguém fora deste pacote pode instanciar um Prontuario
    // diretamente, garantindo que ele só nasça atrelado a um Atendimento.
    Prontuario(String observacoes, String diagnostico, String dataRegistro) {
        this.observacoes = observacoes;
        this.diagnostico = diagnostico;
        this.dataRegistro = dataRegistro;
        this.procedimentos = new ArrayList<>();
        this.informacoesEspecificas = new ArrayList<>();
    }

    public String getObservacoes() {
        return observacoes;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public List<String> getProcedimentos() {
        return procedimentos;
    }

    public List<String> getInformacoesEspecificas() {
        return informacoesEspecificas;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    void adicionarProcedimento(String procedimento) {
        if (procedimentos.size() < 10) {
            procedimentos.add(procedimento);
        }
    }

    void adicionarProcedimentos(String[] procs, int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            if (procedimentos.size() < 10) {
                procedimentos.add(procs[i]);
            }
        }
    }

    public void adicionarInformacaoEspecifica(String informacao) {
        informacoesEspecificas.add(informacao);
    }

    public String exibirResumo() {
        String resumo = "Observacoes: " + observacoes;

        if (!diagnostico.equals("")) {
            resumo = resumo + "\nDiagnostico: " + diagnostico;
        }

        if (procedimentos.size() > 0) {
            resumo = resumo + "\nProcedimentos: ";
            for (int i = 0; i < procedimentos.size(); i++) {
                resumo = resumo + procedimentos.get(i);
                if (i < procedimentos.size() - 1) {
                    resumo = resumo + ", ";
                }
            }
        }

        for (String info : informacoesEspecificas) {
            resumo = resumo + "\n" + info;
        }

        return resumo;
    }
}

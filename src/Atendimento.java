import java.util.ArrayList;

public class Atendimento {
    private int indiceConsulta;
    private String observacoes;
    private String diagnostico;
    private ArrayList<String> procedimentos;

    // registro basico - so observacoes
    public Atendimento(int indiceConsulta, String observacoes) {
        this.indiceConsulta = indiceConsulta;
        this.observacoes = observacoes;
        this.diagnostico = "";
        this.procedimentos = new ArrayList<>();
    }

    public Atendimento(int indiceConsulta, String observacoes, String diagnostico) {
        this.indiceConsulta = indiceConsulta;
        this.observacoes = observacoes;
        this.diagnostico = diagnostico;
        this.procedimentos = new ArrayList<>();
    }

    // registro completo com procedimentos ja definidos
    public Atendimento(int indiceConsulta, String observacoes, String diagnostico,
                       String[] procedimentos) {
        this.indiceConsulta = indiceConsulta;
        this.observacoes = observacoes;
        this.diagnostico = diagnostico;
        this.procedimentos = new ArrayList<>();
        for (int i = 0; i < procedimentos.length; i++) {
            this.procedimentos.add(procedimentos[i]);
        }
    }

    // adiciona um por vez
    public void adicionarProcedimento(String procedimento) {
        if (procedimentos.size() < 10) {
            procedimentos.add(procedimento);
        }
    }

    // adiciona varios de uma vez
    public void adicionarProcedimento(String[] procs, int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            if (procedimentos.size() < 10) {
                procedimentos.add(procs[i]);
            }
        }
    }
    //getters e setters 
    public String getObservacoes(){
        return observacoes; 
    }
    public String getDiagnostico(){
        return diagnostico; 
    }
    public ArrayList<String> getProcedimentos(){
        return procedimentos;
    }
     public int getIndice(){    
        return indiceConsulta;
    } 
    public int getTotalProcedimentos(){
        return procedimentos.size();  
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
        return resumo;
    }
}

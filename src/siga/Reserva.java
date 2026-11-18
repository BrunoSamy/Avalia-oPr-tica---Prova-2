package siga;

/**
 * Reserva de um equipamento do laboratorio.
 *
 * Antes as regras de transicao ficavam em if/else sobre uma String dentro
 * de cada metodo. Agora a Reserva so guarda a fase atual e delega para ela;
 * quem decide o que pode ou nao pode sao as classes de fase
 * (FaseSolicitada, FaseEmUso, FaseEncerrada).
 *
 * Ciclo de vida (o mesmo de antes):
 *   SOLICITADA  --retirar-->  EM_USO
 *   SOLICITADA  --cancelar--> ENCERRADA
 *   EM_USO      --devolver--> ENCERRADA
 *   ENCERRADA e final: recusa as tres operacoes.
 */
public class Reserva {

    private final Usuario solicitante;
    private final Equipamento item;
    private FaseReserva fase;

    public Reserva(Usuario solicitante, Equipamento item) {
        this.solicitante = solicitante;
        this.item = item;
        this.fase = new FaseSolicitada();
    }

    public String getSituacao() {
        return fase.getNome();
    }

    public void retirar() {
        fase = fase.retirar();
    }

    public void devolver() {
        fase = fase.devolver();
    }

    public void cancelar() {
        fase = fase.cancelar();
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public Equipamento getEquipamento() {
        return item;
    }
}

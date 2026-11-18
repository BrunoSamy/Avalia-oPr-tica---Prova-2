package siga;

/**
 * Uma fase do ciclo de vida da Reserva.
 *
 * Cada operacao devolve a proxima fase. Por padrao tudo e recusado; cada
 * fase concreta sobrescreve so o que e permitido nela. Assim, para saber o
 * que pode acontecer em SOLICITADA, por exemplo, basta abrir FaseSolicitada.
 */
abstract class FaseReserva {

    private final String nome;

    protected FaseReserva(String nome) {
        this.nome = nome;
    }

    String getNome() {
        return nome;
    }

    FaseReserva retirar() {
        throw recusar("retirar");
    }

    FaseReserva devolver() {
        throw recusar("devolver");
    }

    FaseReserva cancelar() {
        throw recusar("cancelar");
    }

    // mesma mensagem que a Reserva ja usava antes da refatoracao
    private IllegalStateException recusar(String operacao) {
        return new IllegalStateException(
                "Nao e possivel " + operacao + " em " + nome + ".");
    }
}

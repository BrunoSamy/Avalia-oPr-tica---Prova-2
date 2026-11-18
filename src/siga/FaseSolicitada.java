package siga;

/**
 * SOLICITADA: fase inicial.
 *   retirar  -> EM_USO
 *   cancelar -> ENCERRADA
 *   devolver -> recusado (ainda nem foi retirado)
 */
class FaseSolicitada extends FaseReserva {

    FaseSolicitada() {
        super("SOLICITADA");
    }

    @Override
    FaseReserva retirar() {
        return new FaseEmUso();
    }

    @Override
    FaseReserva cancelar() {
        return new FaseEncerrada();
    }
}

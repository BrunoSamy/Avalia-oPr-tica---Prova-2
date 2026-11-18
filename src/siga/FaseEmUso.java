package siga;

/**
 * EM_USO: o equipamento esta com o usuario.
 *   devolver -> ENCERRADA
 *   retirar e cancelar -> recusados
 */
class FaseEmUso extends FaseReserva {

    FaseEmUso() {
        super("EM_USO");
    }

    @Override
    FaseReserva devolver() {
        return new FaseEncerrada();
    }
}

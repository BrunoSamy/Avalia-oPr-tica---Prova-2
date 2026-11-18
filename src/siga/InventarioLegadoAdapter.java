package siga;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter entre o inventario patrimonial (que nao podemos mexer) e o resto
 * do sistema.
 *
 * Toda a conversao da matriz fica aqui: coluna 0 = codigo, 1 = descricao,
 * 2 = situacao em letra ("D" ou "B", as vezes minuscula). Se o formato
 * mudar ou aparecer outro caso estranho, so esta classe precisa ser
 * corrigida.
 */
public class InventarioLegadoAdapter implements FonteEquipamentos {

    private static final int CODIGO = 0;
    private static final int DESCRICAO = 1;
    private static final int SITUACAO = 2;

    private final InventarioLegadoWS legado;

    public InventarioLegadoAdapter(InventarioLegadoWS legado) {
        this.legado = legado;
    }

    @Override
    public List<Equipamento> listarEquipamentos() {
        List<Equipamento> equipamentos = new ArrayList<>();
        for (String[] linha : legado.consultarEquipamentos()) {
            equipamentos.add(converter(linha));
        }
        return equipamentos;
    }

    private Equipamento converter(String[] linha) {
        Equipamento item = new Equipamento(linha[CODIGO], linha[DESCRICAO]);
        item.setDisponivel(estaDisponivel(linha[SITUACAO]));
        return item;
    }

    // o sistema de origem as vezes manda a letra minuscula, por isso ignoro o caso
    private boolean estaDisponivel(String letra) {
        return letra != null && "D".equalsIgnoreCase(letra.trim());
    }
}

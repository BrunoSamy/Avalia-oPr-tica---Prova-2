package siga;

import java.util.List;

/**
 * Relatorio com a situacao de todo o acervo.
 *
 * A conversao que existia aqui foi para o InventarioLegadoAdapter, entao o
 * relatorio e o catalogo agora enxergam exatamente o mesmo dado.
 */
public class RelatorioAcervo {

    private final FonteEquipamentos fonte;

    public RelatorioAcervo(FonteEquipamentos fonte) {
        this.fonte = fonte;
    }

    public List<Equipamento> listarTodos() {
        return fonte.listarEquipamentos();
    }

    public void imprimir() {
        System.out.println("   Situacao segundo o relatorio:");
        for (Equipamento item : listarTodos()) {
            System.out.println("     " + item + " - "
                    + (item.isDisponivel() ? "disponivel" : "baixado"));
        }
    }
}

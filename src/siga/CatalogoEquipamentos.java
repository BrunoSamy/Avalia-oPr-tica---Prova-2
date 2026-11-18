package siga;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista os equipamentos disponiveis para reserva.
 *
 * Antes esta classe lia a matriz do servico legado e fazia a conversao por
 * conta propria (e so reconhecia "D" maiusculo). Agora ela recebe uma
 * FonteEquipamentos e trabalha direto com objetos Equipamento.
 */
public class CatalogoEquipamentos {

    private final FonteEquipamentos fonte;

    public CatalogoEquipamentos(FonteEquipamentos fonte) {
        this.fonte = fonte;
    }

    public List<Equipamento> listarDisponiveis() {
        List<Equipamento> disponiveis = new ArrayList<>();
        for (Equipamento item : fonte.listarEquipamentos()) {
            if (item.isDisponivel()) {
                disponiveis.add(item);
            }
        }
        return disponiveis;
    }
}

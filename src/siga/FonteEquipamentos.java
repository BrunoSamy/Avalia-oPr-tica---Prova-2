package siga;

import java.util.List;

/**
 * O que o sistema espera de quem fornece os equipamentos.
 *
 * Catalogo e Relatorio dependem so desta interface, entao eles nao sabem
 * (e nem precisam saber) que por tras existe um servico legado que devolve
 * uma matriz de texto.
 */
public interface FonteEquipamentos {

    List<Equipamento> listarEquipamentos();
}

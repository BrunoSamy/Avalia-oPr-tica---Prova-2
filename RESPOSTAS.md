# Respostas — Sistema de Reserva de Laboratorio

Nome completo: Bruno Samy Santos Duarte  RA: ______________

> Preencha **todos** os campos. Campo vazio é campo não avaliado.
> Não apague os títulos nem os rótulos em negrito: a correção procura por eles.

---

# Etapa 1 — Diagnóstico

## Problema 1 — a conversão do inventario patrimonial

**Em que consiste o problema, e o que acontece quando o sistema cresce:**

A mesma conversão (pegar a matriz de texto do inventário e transformar em `Equipamento`) foi escrita duas vezes, uma no catálogo e outra no relatório. Como são duas cópias, uma foi corrigida e a outra não: o relatório usa `equalsIgnoreCase` e o catálogo só aceita "D" maiúsculo, por isso o PAT-1003 (que vem com "d") aparece disponível em um lugar e indisponível no outro. Conforme o sistema cresce e surgem outras telas que precisam dos equipamentos, a tendência é cada uma copiar a conversão de novo. Aí qualquer mudança no formato do inventário (ou outro "defeito" como esse da minúscula) tem que ser lembrada e corrigida em vários lugares, e basta esquecer um para o sistema mostrar dados diferentes para a mesma coisa.

**Princípio SOLID violado:**

Principalmente o SRP (Responsabilidade Única): o catálogo e o relatório, além de fazer o trabalho deles, também ficaram responsáveis por entender e converter o formato do serviço legado. Têm mais de um motivo para mudar. A dependência direta da classe concreta (ver o último campo) também fere o DIP.

**Classes afetadas (pelo nome):**

`CatalogoEquipamentos` e `RelatorioAcervo` (as duas que convertem a matriz), e indiretamente o `Main`, que monta as duas passando o `InventarioLegadoWS`.

**Que outra dependência indevida essas classes têm em relação ao
inventario patrimonial, e por que ela é um problema:**

As duas recebem e guardam a classe concreta `InventarioLegadoWS` no construtor, ou seja, dependem diretamente da implementação de um serviço de outra equipe, e não de uma abstração. Isso viola o DIP (Inversão de Dependência): classes de regra do sistema ficam presas a um detalhe externo. Na prática, se o serviço for trocado (outra versão, um banco de dados, uma API nova) as duas classes precisam ser alteradas, e também fica difícil testar o catálogo ou o relatório com dados falsos, porque elas só aceitam aquela classe específica.

## Problema 2 — o ciclo de vida de Reserva

**Em que consiste o problema, e o que acontece quando o sistema cresce:**

O estado da reserva é uma `String` e cada método público (`retirar`, `devolver`, `cancelar`) tem o seu próprio `if` perguntando em que situação a reserva está. As regras ficam espalhadas: para entender o ciclo de vida inteiro é preciso ler todos os métodos e juntar as peças de cabeça. Se amanhã aparecer uma fase nova (por exemplo "ATRASADA" ou "AGUARDANDO_APROVACAO") ou uma operação nova, vai ser preciso mexer em todos os métodos e acrescentar mais condições, aumentando a chance de quebrar uma transição que já funcionava. Além disso, por ser texto, um erro de digitação em "EM_USO" só seria percebido rodando o programa.

**Princípio SOLID violado:**

OCP (Aberto/Fechado): para incluir um estado novo a classe `Reserva` precisa ser modificada em vários pontos, em vez de ser estendida. Também há um pouco de SRP, já que a `Reserva` guarda os dados e ainda concentra todas as regras de transição.

**Classes afetadas (pelo nome):**

`Reserva`.

---

# Etapa 4 — Justificativa

## Padrão aplicado ao Problema 1

**O problema que ele resolve (descreva antes de nomear o padrão):**

Existe um serviço externo que não podemos alterar e que entrega os dados num formato que não é o que o nosso sistema usa (matriz de `String` com a situação em letra). O sistema, por sua vez, quer trabalhar com uma lista de `Equipamento`. Precisava de uma peça no meio que "traduzisse" uma interface para a outra, num lugar só, para que o catálogo e o relatório pedissem os equipamentos sem saber de onde nem em que formato eles vêm. Criei a interface `FonteEquipamentos` (o que o sistema espera) e a classe `InventarioLegadoAdapter`, que implementa essa interface, guarda o `InventarioLegadoWS` por dentro e faz a conversão (já tratando a minúscula). O catálogo e o relatório agora recebem só `FonteEquipamentos`, e o `InventarioLegadoWS` só aparece no `Main`, na hora de montar.

**Nome do padrão:**

Adapter (padrão estrutural).

**Alternativa descartada e por que ela não resolveria por completo:**

A alternativa seria alterar o próprio serviço legado para ele já devolver `Equipamento` ou mandar sempre a letra maiúscula. Primeiro, não dá: o código é de outra equipe e o enunciado proíbe mexer. Mas mesmo que desse, não resolveria tudo: o catálogo e o relatório continuariam dependendo da classe concreta `InventarioLegadoWS`, então trocar a fonte de dados ou testar com dados falsos continuaria difícil. E o nosso sistema ficaria dependendo de outra equipe manter essa correção nas próximas versões. Outra ideia seria só copiar o `equalsIgnoreCase` para o catálogo, mas isso corrige o sintoma e mantém a duplicação, que é o que causou o erro.

## Padrão aplicado ao Problema 2

**O problema que ele resolve (descreva antes de nomear o padrão):**

O comportamento da reserva muda conforme a fase em que ela está: em SOLICITADA pode retirar ou cancelar, em EM_USO só pode devolver, em ENCERRADA não pode nada. Em vez de cada método perguntar "em que fase eu estou?" com `if`, a ideia é transformar cada fase em um objeto que já sabe o que é permitido nela e qual é a próxima fase. Criei a classe abstrata `FaseReserva`, que por padrão recusa as três operações com a mesma mensagem de antes, e as fases `FaseSolicitada`, `FaseEmUso` e `FaseEncerrada`, que sobrescrevem só o que é permitido. A `Reserva` guarda a fase atual e cada método público virou uma linha (`fase = fase.retirar();`), sem nenhum `if`. O `getSituacao()` devolve o nome da fase, então continua retornando "SOLICITADA", "EM_USO" e "ENCERRADA". Para ler o ciclo de vida basta abrir as classes de fase.

**Nome do padrão:**

State (padrão comportamental).

**Alternativa descartada e por que ela não resolveria por completo:**

Trocar a `String` por um `enum` resolveria o problema dos erros de digitação, porque o compilador passaria a conferir os valores. Só que as regras continuariam nos `if` (ou `switch`) dentro de cada método da `Reserva`, espalhadas do mesmo jeito. Para adicionar uma fase nova ainda seria preciso mexer em todos os métodos, então o problema principal (ciclo de vida difícil de ler e classe que precisa ser modificada a cada mudança) continuaria.
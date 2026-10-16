# UI-V8-PLUS — Calendário Material You v8+

## Origem e escopo

Base: `main`, commit `2a8219e`. Referência: `origin/material-you-v8+`,
commit `3fc7e64`, arquivo `prototypes/material-you-v8-plus/index.html`.
Branch de entrega: `feature/material-you-v8-plus-android`, com destino à `main`,
conforme solicitação do proprietário. O novo PR substitui o PR #7 da UI V8.

A instrução atual autoriza implementar a UI do protótipo no app Android.
O escopo inclui apresentação Compose, cadastro local necessário para o botão
Adicionar, valores e totais exibidos na referência, migração Room e testes.
Não houve merge da branch de protótipo nem do PR #7.

## Comportamento

- Paleta lilás, cartões de resumo, navegação mensal, calendário com segunda-feira
  como primeiro dia, seleção do dia, lista/estado vazio e formulário inferior.
- Interface em português brasileiro e valores em reais, conforme a referência.
- Resumos calculados a partir dos registros do mês visível; nenhum dado fictício
  do HTML é inserido no banco. A data inicial continua sendo a data do dispositivo.
- Cadastro com descrição, direção, valor positivo com no máximo duas casas
  decimais e data válida no formato `dd/mm/aaaa`. Vírgula e ponto são aceitos
  como separadores decimais, sem separadores de milhar.
- Valores persistidos em centavos inteiros. A soma dos resumos usa BigDecimal
  para evitar perda de precisão ou overflow da soma de vários valores.
- Ao salvar em outro mês, a tela navega para a data cadastrada.
- Falhas de gravação mantêm o formulário aberto para nova tentativa.
- Migração Room 1 → 2 adiciona valor opcional e preserva os registros anteriores.
  Registros sem valor são exibidos como “Sem valor”; o resumo da direção também
  mostra “Sem valor” quando houver registros sem valor, evitando um total incompleto.
- Barras de status e navegação são as do Android; os indicadores fictícios do
  HTML não são reproduzidos. A tela rola para acomodar telas menores.
- Dados continuam somente no dispositivo, sem mudança na política de backup.

## Validação

Validação local com Temurin 21, SDK Android 36 e Build Tools 36.0.0:

- `:app:assembleDebug`: passou, APK em `app/build/outputs/apk/debug/app-debug.apk`.
- `:app:testDebugUnitTest`: 30 testes passaram, sem falhas ou testes ignorados.
  Inclui interação Compose, validação de datas/centavos, totais mensais,
  gravação com navegação e falha, e migração real do banco 1 → 2 com reabertura.
- `:app:lintDebug`: passou, 0 erros e 16 avisos sobre versões mais recentes das
  dependências já fixadas no projeto; relatório em `app/build/reports/lint-results-debug.html`.
- `:app:assembleDebugAndroidTest`: passou.
- `git diff --check`: passou.
- Captura visual com renderização nativa Robolectric conferida em
  `build/validation/UI-V8-PLUS/calendar.png`. O código temporário de captura
  foi removido antes da execução final dos 30 testes.

Os testes instrumentados foram compilados, mas não executados em dispositivo
ou emulador. Os artefatos e relatórios de build são locais e ignorados pelo Git.
A entrega segue por pull request para `main`, sem merge automático.

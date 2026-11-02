# Cobertura do Material 3 · 1.0.2

Este starter não é uma implementação completa de Material 3/Expressive. A cobertura é explícita por camada.

| Recurso | Web | Android |
|---|---|---|
| Cor e tema | Material Color Utilities, seed/imagem local, claro/escuro | Compose Material3 1.4.0, Dynamic Color API 31+, fallback |
| Botões, ícones, FAB | Catálogo oficial, variantes e estados | Controles nativos do showcase |
| Chips, checkbox, switch, radio, slider | Componentes oficiais com estado local | Controles de exemplo do app |
| Campos e seletores | Filled/outlined text field e select; field primitivos | OutlinedTextField e menu de prioridade |
| Listas, divisores, abas | List/item, divider, primary/secondary tabs | ListItem, HorizontalDivider, PrimaryTabRow |
| Menu e submenu | Menu ancorado e submenu demonstrativos | DropdownMenu |
| Diálogo | Oficial no catálogo; HTML no laboratório | AlertDialog e DatePickerDialog |
| DatePicker, TimePicker | Não incluídos no catálogo estável | Amostras nativas em Explorar |
| Bottom sheet e tooltip | Não incluídos no catálogo estável | ModalBottomSheet e TooltipBox |
| Progresso, elevação, foco, ripple | Componentes oficiais e exemplos CSS autorais | Recursos nativos nos controles |
| Seleção conectada | Animação de fundo e ripple na galeria | Demo nativa, escala de animação do sistema |
| Idiomas | Laboratório pt/en/es; catálogo pt | Showcase pt/en/es |

O catálogo integra os **43 módulos** exportados por `@material/web@2.5.0/all.js`, incluindo containers e primitivos, não 43 famílias independentes. Imports individuais evitam carregar o catálogo inteiro nos exemplos menores. Veja [integração](MATERIAL_WEB.md).

## Recursos preservados

Tokens semânticos, cores locais por imagem, exportação de tokens, morph, galeria painel/lista/detalhe, fade/deslizar/escala com replay e duração, elevação de 0 a 5, layout responsivo, preferências de tema/idioma e exemplos mínimos.

## Limites reais

Dados são fictícios. Não há backend, login, pagamentos, agendamento, exportação real por menus ou persistência de negócio. Estados do novo showcase Android usam rememberSaveable para recriação da Activity, não banco de dados. Fontes de ícones externas não são necessárias ao catálogo.

Material Web está em manutenção. O fork é uma referência separada, não uma biblioteca oficial nova deste projeto. O kit não lê o wallpaper Android a partir do navegador. Busca de negócio, navigation rail/bar oficiais web, layouts foldable, gestos complexos e componentes Expressive adicionais precisam de trabalho específico.

APIs de showcase Android podem exigir opt-in experimental explícito mesmo com dependência estável. Testes automatizados não substituem TalkBack, leitor de tela, contraste, foco, tamanhos de toque e dispositivos reais. Movimento reduzido é respeitado no laboratório e seleção conectada; o catálogo oculta o spinner indeterminado nesse modo, mas componentes oficiais mantêm o comportamento de sua biblioteca.

## Termux e agentes

Leia `AGENTS.md` antes de alterações. Peça uma tarefa específica, confira o diff e execute os testes. Não forneça tokens ou chaves a prompts.

# Cobertura do Material 3 neste starter

O Material 3 é um sistema completo, não um tema que se liga com uma opção. Este repositório implementa uma amostra prática e aponta como ampliar. A lista de componentes e estágio de suporte mudam com as versões de cada plataforma.

## Incluído no kit
- Papéis semânticos de cor: esquema claro/escuro e paletas criadas pela biblioteca oficial Material Color Utilities para a web; Dynamic Color nativo no Android 12+.
- Forma: tokens de cantos, cards, chips e uma amostra de morph decorativo (web e Compose).
- Tipografia: escala padrão do Material 3; personalize escala, pesos e hierarquia para a identidade do produto.
- Superfícies, botões, chips/filtros, switches, slider, progresso, FAB, navegação e diálogo/snackbar de exemplo.
- Seleção conectada: fundo preenchido que desliza entre botões na web e no Android (Explorar), com ripple na posição do ponteiro na web e ripple nativo no app. No Android, a animação segue a escala de duração do sistema.
- Movimento com transições curtas, respeitando `prefers-reduced-motion` na web. A galeria web inclui prévias de painel/lista/detalhe com dados explicitamente fictícios, seleção compartilhada entre botões, transições manuais (fade, deslizar e escala), replay, duração configurável e elevação de 0 a 5.
- Layout adaptável: navegação lateral em telas maiores e inferior em telas pequenas (web responsiva); no Android, Scaffold e NavigationBar.
- Acessibilidade: rótulos semânticos, estado de botões, foco e respeito a movimento reduzido — ainda exige teste humano com leitor de tela e contraste.

## Recursos que o app real deve acrescentar
Componentes não estão todos expostos num único showcase. A galeria é uma demonstração visual local: não representa navegação de aplicativo, dados reais, persistência, gestos complexos ou implementação oficial do Material Web. Conforme o produto pedir, integre menus, campos de texto, date/time pickers, search, bottom sheets, listas, tabs, tooltips, loading indicators, navegação adaptativa, layouts para tablet/foldable e componentes Expressive atuais. Consulte as referências oficiais e a versão da biblioteca antes de implementar; algumas APIs têm estágio experimental.

Na web, Material Web está em manutenção segundo o próprio site da biblioteca. Este starter usa CSS semântico próprio + Material Color Utilities oficial para esquemas de cor; é uma opção de implementação, não uma substituição oficial completa de Material Web. O kit não lê a paleta do wallpaper do Android em uma página web; o usuário escolhe uma cor/imagem explicitamente.

## Termux: pedir uma alteração a um agente
Na raiz do repo, use o prompt em `AGENTS.md` como instruções do projeto. Peça tarefas específicas, por exemplo: “Implemente um DatePicker acessível no exemplo Android usando a versão estável disponível, atualize a documentação e rode `gradle -p android assembleDebug`. Não altere dependências fora do necessário.” Revise o diff, não forneça tokens e não aceite comandos que apaguem dados sem entender.

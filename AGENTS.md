# Guia para agentes de programação neste repositório

Leia este arquivo antes de editar. O produto é um starter educativo open source para Material 3 web + Android.

## Princípios
- Consulte `README.md`, `docs/COMPONENTES.md` e os arquivos da plataforma antes de mudar arquitetura.
- Use APIs e tokens Material reais quando disponíveis; não invente nomes de API nem diga que um mock imita implementação oficial.
- Web: respeite tokens CSS `--md-sys-*`, preferências de tema, responsividade, acessibilidade e `prefers-reduced-motion`. Gere cores com `@material/material-color-utilities`, nunca com HSL improvisado.
- Android: Compose Material 3, Dynamic Color no Android 12+, fallback, estados acessíveis e componentes nativos. Não promova APIs alpha/beta a padrão sem informar e justificar.
- Preserve compatibilidade com Termux, evite ferramentas pesadas e explique comandos com o diretório correto.
- Nunca leia, imprima, comite ou envie `.env`, credenciais, chaves, cookies, keystores, `local.properties`, `~/.ssh` ou tokens. Não execute comandos destrutivos sem autorização expressa.
- Não invente resultado de build/teste. Rode o que estiver disponível e registre limitações com clareza.

## Validação mínima
- Web: `cd web && npm install && npm run build`; abra a demo ou valide em viewport mobile e desktop.
- Android: `gradle -p android assembleDebug` (JDK 17, SDK 36); examine o log até o resultado final.
- Verifique links, contraste e leitura de foco; confira `git diff --check` e `git status` antes de commit.

## Entrega
Resuma arquivos, comportamento visível, testes realmente executados e itens que dependem do usuário. Não faça push/commit sem pedido explícito do dono do projeto.

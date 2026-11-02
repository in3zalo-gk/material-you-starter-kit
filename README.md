# Material You Starter Kit

Kit open source para começar interfaces inspiradas no **Material 3 (M3)** e no **Material 3 Expressive** da Google, com exemplos para sites e apps Android nativos.

> Material 3 Expressive é uma evolução do Material 3, não uma nova versão separada. Este repositório é um starter independente e **não é afiliado, mantido ou endossado pela Google**. Ele demonstra princípios e componentes; não redistribui o sistema proprietário de apps Google.

## Prévia da interface web

A imagem abaixo é uma captura real da demo publicada — não é um mockup de app Android. O layout se adapta a telas menores e oferece tema claro e escuro.

<p align="center"><img src="assets/web-preview.png" alt="Captura da demo web com componentes Material 3 em tema claro" width="100%"></p>

[**Abrir demonstração web**](https://in3zalo-gk.github.io/material-you-starter-kit/)

## O que vem incluído

- **Demo web construída com Vite**: tema claro/escuro, esquema dinâmico com Material Color Utilities oficial, seleção de cor ou imagem local processada no navegador, componentes interativos e layout responsivo.
- **Tokens compartilháveis** em CSS custom properties para cor, tipografia, forma e elevação.
- **Interface localizada em português, inglês e espanhol**: seleção automática pelo idioma do navegador/sistema, opção manual e preferência salva localmente; sem enviar textos para tradutores externos.
- **Exemplo de animação spring** no Compose, inspirado no feedback tátil do Prismal, inspirado no feedback tátil do [Prismal](https://github.com/styropyr0/Prismal), sem incluir sua dependência OpenGL de captura/renderização de fundo. A web inclui uma amostra de vidro com CSS e fallback opaco; não é refração física nem usa a biblioteca Prismal.
- **Exemplo Android em Kotlin + Jetpack Compose Material 3 1.4.0 estável**, Dynamic Color no Android 12+ e fallback em versões anteriores.
- **Referências e guia em português** para adaptar ao seu produto sem copiar marcas ou telas do Google.
- **GitHub Pages + CI Android**: workflows publicam a web e compilam um APK debug em artifact nas mudanças correspondentes.

## Ver a demo localmente

Para rodar a demo local, instale Node.js LTS e execute na raiz do projeto:

```bash
cd web
npm install
npm run dev -- --host 0.0.0.0
```

Abra o endereço local que o Vite indicar (no próprio aparelho, normalmente `http://127.0.0.1:5173`). Para gerar a versão de produção, rode `npm run build` dentro de `web/`.

## Usar no seu site

1. Copie `web/tokens.css` e `web/material-you.css` e instale `@material/material-color-utilities` (`npm install @material/material-color-utilities`) para usar o gerador de esquema incluído.
2. Inclua os estilos no `<head>`:

```html
<link rel="stylesheet" href="tokens.css">
<link rel="stylesheet" href="material-you.css">
```

3. Aplique os tokens semânticos em vez de fixar cores em cada componente. Para derivar esquema dinâmico em app web com build, use a API do pacote:

```js
import { argbFromHex, themeFromSourceColor, applyTheme } from "@material/material-color-utilities";
const theme = themeFromSourceColor(argbFromHex("#6750a4"));
applyTheme(theme, { target: document.documentElement, dark: false });
```

```css
.meu-botao {
  color: var(--md-sys-color-on-primary);
  background: var(--md-sys-color-primary);
  border-radius: var(--md-sys-shape-corner-full);
  padding: 0.75rem 1.25rem;
}
```

Os tokens seguem os papéis de cor do Material; a demo usa a biblioteca Material Color Utilities para criar tonal palettes por seed color ou extrair a cor de uma imagem local. O site compila com Vite; o Pages publica `web/dist`.

## Usar no Android

Abra a pasta `android/` no Android Studio. O app de exemplo usa Jetpack Compose e `androidx.compose.material3:material3:1.4.0`. Consulte [android/README.md](android/README.md), [docs/IDIOMAS.md](docs/IDIOMAS.md) e [docs/TERMUX_AI.md](docs/TERMUX_AI.md). O workflow Android compila um APK debug e publica-o como artefato em cada alteração à pasta Android.

- Em Android 12/API 31+, `dynamicColor = true` usa a paleta do sistema baseada no papel de parede.
- Em versões anteriores, ou se desativado, usa uma paleta de fallback incluída no exemplo.
- Os componentes Compose usam os papéis de `MaterialTheme.colorScheme`.

## Idiomas e localização

A tradução demonstrativa fica organizada em [web/i18n.js](web/i18n.js) na web e [AppTranslations.kt](android/app/src/main/java/dev/saynoob/materialyou/AppTranslations.kt) no Compose. No primeiro uso, a interface segue o idioma do navegador ou dispositivo, com fallback em português; o seletor deixa escolher o idioma e memoriza a escolha localmente. Não é tradução de conteúdo livre por IA nem usa uma API online. Consulte o [guia de idiomas](docs/IDIOMAS.md) para adicionar outras línguas.

## Personalização e acessibilidade

- Mude a cor pelo seletor e deixe Material Color Utilities atualizar os papéis semânticos; evite ajustar papéis de primeiro plano/fundo sem revisar contraste.
- Use sempre pares de papéis compatíveis (`primary`/`onPrimary`, `surface`/`onSurface`).
- Verifique contraste, foco por teclado, leitores de tela e tamanhos de toque no seu contexto real. Tokens não substituem testes de acessibilidade.
- Não aplique cores dinâmicas de fundo a fundos fotográficos sem avaliar legibilidade.

## Termux e assistência de IA

O guia [Termux + IA](docs/TERMUX_AI.md) mostra como editar, validar e enviar mudanças pelo telefone, além de como pedir ajuda a um agente com contexto em [AGENTS.md](AGENTS.md). A pasta do projeto inclui orientações de segurança e não exige conectar uma IA específica.

## Guias incluídos

- [Catálogo de componentes, recursos e limites](docs/COMPONENTES.md)
- [Idiomas e localização](docs/IDIOMAS.md)
- [Termux + IA para programar com segurança](docs/TERMUX_AI.md)
- [Instruções para agentes de código](AGENTS.md)

## Referências oficiais

- [Material Design 3](https://m3.material.io/)
- [Introdução ao Material 3 Expressive](https://m3.material.io/blog/building-with-m3-expressive)
- [Material 3 no Jetpack Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [Material Web](https://material-web.dev/) — a documentação informa que a biblioteca está em modo de manutenção; por isso a demo web deste kit usa CSS próprio e não depende dela.
- [Material Color Utilities (biblioteca oficial)](https://github.com/material-foundation/material-color-utilities)
- [Notas de versão Compose Material 3](https://developer.android.com/jetpack/androidx/releases/compose-material3)
- [Material Theme Builder](https://material.io/material-theme-builder)

**Inspiração visual:** use as diretrizes e componentes descritos pela Google; a imagem acima é uma captura da demo web deste projeto, não de Gmail, Fotos ou outros produtos Google. A captura no README é da web; o app Android ainda não tem screenshot do emulador incluída.

## Licença

MIT. Veja [LICENSE](LICENSE). As marcas e materiais vinculados da Google pertencem aos respectivos titulares.

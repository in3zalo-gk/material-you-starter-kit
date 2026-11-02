<p align="center"><img src="assets/logo.svg" width="96" alt="m3 kit"></p>
<h1 align="center">m3 kit</h1>
<p align="center">Material 3 para web e Android. Componentes reais, temas dinâmicos e espaço para sua identidade.</p>
<p align="center">
  <a href="https://github.com/in3zalo-gk/material-you-starter-kit/releases/latest"><img alt="Release" src="https://img.shields.io/github/v/release/in3zalo-gk/material-you-starter-kit?color=2f6b46"></a>
  <a href="LICENSE"><img alt="Código do kit: MIT" src="https://img.shields.io/badge/kit-MIT-2f6b46"></a>
  <a href="https://github.com/in3zalo-gk/material-you-starter-kit/actions/workflows/stable.yml"><img alt="Build e testes" src="https://github.com/in3zalo-gk/material-you-starter-kit/actions/workflows/stable.yml/badge.svg"></a>
  <img alt="Material Web 2.5.0" src="https://img.shields.io/badge/Material_Web-2.5.0-445e91">
  <img alt="Compose Material3 1.4.0" src="https://img.shields.io/badge/Compose_Material3-1.4.0-445e91">
</p>
<p align="center"><a href="https://in3zalo-gk.github.io/material-you-starter-kit/">Demo</a> · <a href="https://in3zalo-gk.github.io/material-you-starter-kit/catalog.html">Catálogo interativo</a> · <a href="https://github.com/in3zalo-gk/material-you-starter-kit/releases/latest">Downloads</a> · <a href="#comece-em-minutos">Quick start</a> · <a href="#documentação">Documentação</a></p>

<img src="assets/web-preview.png" width="100%" alt="Captura real do laboratório de temas m3 kit">

## Uma base, duas plataformas

Um starter demonstrativo open source para construir interfaces com Material 3. A **1.0.2** acrescenta um catálogo web com os **43 módulos estáveis exportados por Material Web 2.5.0**, exemplos de integração e mais componentes nativos no Android. O laboratório original, a galeria e os exemplos mínimos continuam disponíveis.

Não é um produto SaaS pronto: não inclui backend, login, pagamentos ou dados de clientes. Não há edição paga escondida. O código próprio é MIT; as dependências mantêm suas licenças, inclusive Apache-2.0 no Material Web.

## Explore

| Área | O que você pode experimentar |
| --- | --- |
| [Laboratório de tema](https://in3zalo-gk.github.io/material-you-starter-kit/#laboratorio) | Cor da marca ou imagem local, claro/escuro, Material Color Utilities e exportação de tokens CSS |
| [Catálogo Material Web](https://in3zalo-gk.github.io/material-you-starter-kit/catalog.html) | Busca, cores ao vivo, botões, campos, seletores, menus/submenus, diálogo, listas, abas, chips, sliders, progresso, ícones, FAB, foco, ripple e elevação |
| [Galeria de interação](https://in3zalo-gk.github.io/material-you-starter-kit/#galeria) | Painel/lista/detalhe fictícios, seleção conectada, fade/deslizar/escala, duração e níveis de elevação |
| Android → Explorar | Seleção conectada, formulário/lista, prioridades, DatePicker, TimePicker, bottom sheet e tooltips |
| [HTML mínimo](https://in3zalo-gk.github.io/material-you-starter-kit/examples/minimal.html) | Tokens e CSS do kit sem a demonstração completa |
| [Material Web mínimo](https://in3zalo-gk.github.io/material-you-starter-kit/examples/material-web.html) | Imports individuais de botão e campo oficial, sem carregar o catálogo inteiro |

### Componentes, não apenas uma imagem

![Ilustração da seleção conectada: o fundo acompanha o botão escolhido](assets/connected-selection.svg)

O fundo desliza até o botão selecionado; ripple e estado acompanham a interação. A imagem ilustra o movimento. Experimente a implementação real na galeria web ou em **Explorar → Seleção conectada** no Android.

### Catálogo oficial, identidade própria

O kit integra `@material/web@2.5.0` como dependência fixada e usa os mesmos módulos estáveis presentes no [fork de referência](https://github.com/in3zalo-gk/material-web). Não copia o branding nem reempacota o fork como uma biblioteca criada aqui. Os componentes são carregados por imports individuais; o catálogo demonstra todos, enquanto o exemplo mínimo carrega somente dois.

**Idioma:** laboratório e app Android em pt/en/es. O novo catálogo e o exemplo Material Web estão em português. Os ícones do catálogo usam símbolos de texto locais, sem exigir Google Fonts.

## Comece em minutos

### Web

Requer Git, Node **20.19+ ou 22.12+** e npm.

```sh
git clone https://github.com/in3zalo-gk/material-you-starter-kit.git
cd material-you-starter-kit/web
npm ci
npm run dev
```

Abra a URL indicada pelo Vite. Explore `/catalog.html`, `/examples/minimal.html` ou `/examples/material-web.html`.

```sh
npm run build
# publique o conteúdo de dist/ em uma hospedagem estática
```

Não abra os arquivos de desenvolvimento via `file://`. O ZIP `web-ready` já inclui o build e os avisos de terceiros.

### Android

Requer JDK **17**, SDK **36** e Gradle **8.13** instalado. Abra `android/` no Android Studio ou, na raiz do repositório:

```sh
gradle -p android assembleDebug lintDebug
```

O repositório não inclui Gradle Wrapper. Android mínimo **23**; Dynamic Color nativo disponível a partir do Android **12 / API 31**, com fallback verde nas versões anteriores. [Guia Android](android/README.md).

## Personalize

- **Marca e cores:** escolha uma seed ou uma imagem local no laboratório. A imagem é processada no navegador, não enviada para um servidor.
- **Tokens:** use pares semânticos `--md-sys-color-primary` / `--md-sys-color-on-primary`, em vez de cores soltas.
- **Componentes oficiais:** importe apenas os módulos necessários, seguindo `web/examples/material-web.js`.
- **Layouts e movimento:** adapte o conteúdo, as formas e os tempos. O laboratório respeita `prefers-reduced-motion`; no Android a seleção conectada segue a escala do sistema.
- **Android:** use tema sistema/claro/escuro, Dynamic Color e Compose Material 3. Algumas APIs de showcase exigem opt-in experimental explícito.

## Downloads 1.0.2

| Pacote | Para quê |
| --- | --- |
| `opensource-complete.zip` | Código próprio completo, documentação, assets e scripts; dependências resolvidas pelos manifests |
| `web-source.zip` | Fontes web e lockfile |
| `web-ready.zip` | Site compilado, pronto para hospedagem estática |
| `android-source.zip` | Projeto Android completo |
| `web-example.zip` | Ambiente web completo com os dois exemplos mínimos |
| `android-example.zip` | Ambiente Android e exemplo separado |
| `android-complete-debug.apk` | Showcase Android para instalação manual |
| `android-example-debug.apk` | Exemplo mínimo Android, com applicationId separado |
| `SHA256SUMS.txt` | Integridade de cada arquivo distribuído |

Todos os nomes começam com `m3-kit-1.0.2-`. APKs são **debug assinados com chave de desenvolvimento**, não builds de produção nem de Play Store. Mantenha sua chave de produção fora do repositório. [Escolher um download](docs/DOWNLOADS.md).

## Documentação

[Componentes e cobertura](docs/COMPONENTES.md) · [Exemplos](docs/EXAMPLES.md) · [Integração Material Web](docs/MATERIAL_WEB.md) · [Idiomas](docs/IDIOMAS.md) · [Android](android/README.md) · [Termux + agentes](docs/TERMUX_AI.md) · [Changelog](CHANGELOG.md) · [Notas 1.0.2](docs/RELEASE-1.0.2.md)

## Qualidade e limites

A publicação passa por build web, testes de navegador, auditoria de dependências, build Android e lint. Veja o badge e o histórico do workflow para o resultado de cada commit. Testes automatizados não substituem leitor de tela, contraste, dispositivos reais e revisão do conteúdo.

**Cobertura honesta:** os 43 módulos são o catálogo estável exportado por `@material/web`, não todo o sistema Material 3 ou Material 3 Expressive. Date/time pickers e sheets Android não ganham equivalentes web automaticamente. Não há autenticação, persistência de negócio, exportação real de menus demonstrativos ou garantias de acessibilidade automática. [Matriz detalhada](docs/COMPONENTES.md).

Material Web está em manutenção conforme seu projeto upstream. Este é um projeto independente, sem afiliação ou endosso da Google. Versões 1.0.0 e 1.0.1 permanecem inalteradas.

## Referências e licença

[Material 3](https://m3.material.io/) · [Material Web](https://github.com/material-components/material-web) · [Compose Material3](https://developer.android.com/develop/ui/compose/designsystems/material3) · [Material Color Utilities](https://github.com/material-foundation/material-color-utilities)

MIT © 2026 SayNoob_TYT para o código próprio. [LICENSE](LICENSE) · [Avisos e licenças de terceiros](THIRD_PARTY_NOTICES.md). Marcas de terceiros pertencem aos seus titulares.

# Idiomas na interface

O kit demonstra **tradução local da interface**, não tradução automática de conteúdo arbitrário nem de páginas externas. Não envia texto para serviços de tradução.

## Web

`web/i18n.js` centraliza as strings da demonstração em português (idioma base), inglês e espanhol. No primeiro acesso, a opção **Automático** escolhe o idioma preferido informado pelo navegador; a escolha manual fica salva no `localStorage`. Português é o fallback. A tradução atualiza também `lang` no HTML, rótulos acessíveis e mensagens de estado.

Para acrescentar uma língua:

1. Adicione um dicionário em `messages` usando exatamente as frases existentes no português.
2. Inclua a opção no seletor `#languageSelect` do `web/index.html`.
3. Acrescente a regra de detecção em `detectLanguage()` e teste tudo com build Vite e leitor de tela.

## Android

O exemplo Compose guarda as traduções da interface em `AppTranslations.kt` e usa idioma do sistema como padrão, com fallback para português. A página **Personalizar → Idioma do app** permite escolher Automático, Português, English ou Español; uma seleção manual é persistida localmente no app. O controle é independente do idioma do sistema e a preferência é privada ao app.

As traduções são strings fixas do exemplo, não tradução por IA ou serviço online. Em um app de produção, use recursos `strings.xml` com qualificadores de locale e APIs de preferência de idioma por app do Android quando apropriado; cubra plurais, formatação de números/datas, expansão de texto e RTL.

## Princípios

- Não usar tradução em rede silenciosa; preserve privacidade e permita seleção manual.
- Manter português como fallback explícito.
- Não traduzir nomes de marca, identificadores de código ou termos Material cuja tradução causaria ambiguidade.
- Tradução de interface não substitui localização: revisar datas, números, plurais, acessibilidade e layout.

## Referência de movimento

O repositório [Prismal](https://github.com/styropyr0/Prismal) demonstra vidro líquido em Android com OpenGL ES, captura de conteúdo atrás da vista e feedback de mola. Para este starter, a referência foi aplicada como **interação spring leve** no cartão Compose, sem puxar a dependência de renderização/captura de fundo: ela seria excessiva para a tarefa de localização e poderia aumentar custo de GPU/memória. A demo web já mostra morph de forma com CSS e respeita `prefers-reduced-motion`. Prismal não implementa a tradução do kit, nem foi incluído como dependência.

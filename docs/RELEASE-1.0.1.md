Atualização **1.0.1** do m3 kit, open source MIT para web e Android.

### Novidades
- **Seleção conectada**: fundo colorido desliza até o botão selecionado dentro do grupo, na web e no Android (Explorar → Seleção conectada).
- Ripple no ponto clicado na web e ripple nativo no Android.
- Galeria web com páginas fictícias, controles de movimento, estados de botões e elevação.
- README com ilustração animada e link para a demo interativa.
- Preservados temas, idiomas pt/en/es, persistência e preferências de movimento.
- Corrigido o pacote de exemplo web para incluir todos os módulos e estilos.

### Downloads
- **web-ready.zip**: site compilado para hospedagem estática.
- **opensource-complete.zip**: fontes completas MIT.
- **web-source.zip / android-source.zip**: fontes por plataforma.
- **web-example.zip / android-example.zip**: exemplos com ambiente de build.
- **android-complete-debug.apk**: app completo com seleção conectada.
- **android-example-debug.apk**: exemplo mínimo; não contém a galeria completa.
- **SHA256SUMS.txt**: checksums dos oito pacotes.

APKs de demonstração assinados com chave debug, não binários de produção. Como a chave debug do CI pode mudar entre builds, a atualização pode exigir desinstalar o APK anterior (isso remove suas preferências locais). Ambos usam versionName 1.0.1 e versionCode 2.

### Validação exigida antes da publicação
Build web, 7 testes Chromium, npm audit (high), build dos dois APKs e Android lint. Sem teste em aparelho físico ou certificação de acessibilidade. A v1.0.0 permanece intacta; não há edição paga ou backend.

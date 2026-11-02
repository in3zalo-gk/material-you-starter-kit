# Exemplos mínimos

## Web

Com `web` como diretório atual: `npm ci && npm run dev`. Abra `/examples/minimal.html`. O build inclui a mesma página em `dist/examples/minimal.html`.

O exemplo importa tokens e estilos, sem depender do laboratório completo. Adapte cores e conteúdo ao seu produto.

## Material Web oficial

Abra `/examples/material-web.html`: importa botão preenchido e campo contornado individualmente, sem carregar todo o catálogo. O texto fica local e não há backend. Veja `web/examples/material-web.js` para adaptar os imports.

## Android

`gradle -p android :example:assembleDebug` gera `android/example/build/outputs/apk/debug/example-debug.apk`. O applicationId separado permite instalar junto com o showcase. SDK mínimo 23; cor dinâmica disponível a partir de 31.

Os ZIPs de exemplo incluem os arquivos do ambiente para compilação. Não incluem SDK, Gradle instalado, cache de dependências ou chave privada de produção.

# Downloads 1.0.2

O código próprio é open source MIT; dependências têm suas licenças preservadas em THIRD_PARTY_NOTICES.md. “Completo” significa o showcase e suas fontes, não uma licença diferente.

| Arquivo | Uso |
|---|---|
| opensource-complete.zip | Todo o código, documentação e identidade |
| web-ready.zip | Site já compilado, inclui catálogo e exemplos mínimos |
| web-source.zip | Projeto web editável com lockfile |
| android-source.zip | Projeto Android com showcase e exemplo |
| web-example.zip | HTML mínimo e ambiente de desenvolvimento web |
| android-example.zip | Exemplo nativo e projeto Gradle necessário |
| android-complete-debug.apk | Showcase Android instalável |
| android-example-debug.apk | Exemplo mínimo Android instalável |
| SHA256SUMS.txt | Integridade dos downloads |

Os nomes têm prefixo `m3-kit-1.0.2-`. APKs debug são assinados automaticamente com chave de desenvolvimento do build. Não são adequados para distribuição na Play Store; builds futuros podem exigir desinstalar o anterior por troca da chave debug. APKs não pedem permissões de internet nem coletam dados. O ZIP de exemplo Android inclui o app completo para que o settings Gradle funcione sem edições.

Web pronta deve ser servida por HTTP/HTTPS; não basta abrir o HTML com file://. Fontes web precisam de Node/npm, fontes Android de JDK/SDK/Gradle.

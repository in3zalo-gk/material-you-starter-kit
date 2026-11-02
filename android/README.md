# Android nativo

O diretório contém duas aplicações Compose independentes, ambas usando **Material 3 estável 1.4.0**, Kotlin 2.0.21, AGP 8.13.0, `compileSdk 36`, `minSdk 23` e bytecode Java/Kotlin 17.

- `:app` (`dev.saynoob.materialyou`) é a demonstração principal. Ela mantém no aparelho o modo de tema **Sistema / Claro / Escuro** e a opção de **Dynamic Color**. Em Android 12 (API 31) ou superior, Dynamic Color usa a paleta do sistema; desativado ou em versões anteriores, a demo usa sua paleta verde clara/escura de fallback.
- `:example` (`dev.saynoob.materialyou.example`) é uma aplicação mínima separada, com seu próprio APK e identidade, para testar Compose + Material 3 sem depender do app principal.

As escolhas de tema do app principal usam `SharedPreferences`; estados de navegação e controles usam `rememberSaveable`, portanto são recuperados em recriação de Activity, como rotação. O exemplo mínimo mantém apenas o controle temporário de Dynamic Color durante recriações.

## Compilar

Abra **`android/`** no Android Studio com **JDK 17** e SDK Platform 36 instalado. Pelo terminal, na raiz do repositório, use uma instalação local do Gradle 8.13 ou compatível:

```bash
gradle -p android assembleDebug
```

Para compilar apenas uma aplicação:

```bash
gradle -p android :app:assembleDebug
gradle -p android :example:assembleDebug
```

APKs debug gerados:

```text
android/app/build/outputs/apk/debug/app-debug.apk
android/example/build/outputs/apk/debug/example-debug.apk
```

Não há chaves de assinatura ou segredos no projeto. Para distribuição, configure uma assinatura de release fora do controle de versão.

## Notas de API

O projeto não habilita APIs Compose/Material 3 alpha ou preview. O tema, as cores dinâmicas e os componentes usados são APIs estáveis da dependência `androidx.compose.material3:material3:1.4.0`. Teste contraste, leitor de tela, foco e áreas de toque no produto final, especialmente com paletas dinâmicas de usuários.

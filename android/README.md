# Exemplo Android nativo

App de demonstração em Kotlin + Jetpack Compose Material 3, com tema do sistema, paleta dinâmica do papel de parede (Android 12/API 31+) e fallback em versões anteriores. Há uma tela funcional com navegação inferior, filtros, Slider, Switch, cartões, progress indicator, snackbar e diálogo.

## Compilar

Abra `android/` no Android Studio com JDK 17. Este projeto usa Android Gradle Plugin 8.13.0, Gradle 8.13+, compileSdk 36, minSdk 23 e Material 3 estável 1.4.0 (consultada em 2026-11-02). Para gerar o APK pelo terminal, com Gradle 8.13 instalado: `gradle -p android assembleDebug`. O APK sai em `android/app/build/outputs/apk/debug/app-debug.apk`.

Material 3 1.4.0 estável inclui aprimoramentos do sistema de motion, componentes e suporte dinâmico de cor. Recursos Compose explicitamente Expressive estão em evolução e alguns exigem dependências preview; este exemplo não ativa APIs de preview por padrão. Use uma versão experimental somente em branch de teste e confirme release notes, opt-ins e suporte à versão Android antes.

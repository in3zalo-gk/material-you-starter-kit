# Exemplo Android

App mínimo em Kotlin com Jetpack Compose e Material 3. Abra esta pasta no Android Studio recente com suporte a Gradle 8.9+, JDK 17 e Android SDK 35 instalado. O Gradle Wrapper não está incluído nesta versão inicial; o Android Studio pode configurar o wrapper ao importar ou use `gradle wrapper` com Gradle instalado.

Para executar, selecione a configuração `app` e rode em emulador/dispositivo com API 23+. O Dynamic Color usa a paleta do sistema a partir do Android 12 (API 31); nos demais casos aplica o esquema de fallback.

A versão de `androidx.compose.material3` está definida no `gradle/libs.versions.toml`. Confirme a versão estável atual no [Android Developers](https://developer.android.com/jetpack/androidx/releases/compose-material3) antes de atualizar. APIs Expressive podem mudar; mantenha versões alinhadas com o BOM e valide a compilação ao atualizar.

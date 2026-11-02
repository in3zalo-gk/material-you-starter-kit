package dev.saynoob.materialyou

import android.os.Build
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

val LocalAppLanguage = staticCompositionLocalOf { "pt" }
fun localized(source: String, language: String): String {
    val english = mapOf(
        "Componentes" to "Components", "Personalizar" to "Personalize", "Começar" to "Get started", "Ver diálogo" to "View dialog",
        "MATERIAL 3 • COMPOSE" to "MATERIAL 3 • COMPOSE", "Design que responde a você." to "Design that responds to you.",
        "A paleta dinâmica acompanha o papel de parede em Android 12 ou superior." to "The dynamic palette follows your wallpaper on Android 12 or later.",
        "Um bom começo" to "A good start", "Uma boa base" to "A good foundation", "Cores semânticas, hierarquia e controles acessíveis." to "Semantic colors, hierarchy, and accessible controls.",
        "Testar ação" to "Try action", "Controles" to "Controls", "Notificações" to "Notifications", "Intensidade" to "Intensity",
        "Filtros" to "Filters", "Tudo" to "All", "Design" to "Design", "Android" to "Android", "Progresso" to "Progress",
        "68% concluído" to "68% complete", "Ação secundária" to "Secondary action", "Listas e superfícies continuam legíveis em temas dinâmicos." to "Lists and surfaces remain legible with dynamic themes.",
        "O tema claro/escuro segue a preferência do sistema." to "Light/dark theme follows system preference.", "Personalização" to "Personalization",
        "Cores do sistema" to "System colors", "Dynamic Color está ativo: o app usa as cores fornecidas pelo sistema." to "Dynamic Color is active: the app uses colors provided by the system.",
        "Este Android não fornece Dynamic Color; o app usa a paleta Material de fallback." to "This Android version does not provide Dynamic Color; the app uses the Material fallback palette.",
        "Versão Android: ${Build.VERSION.SDK_INT}" to "Android version: ${Build.VERSION.SDK_INT}", "Papéis semânticos" to "Semantic roles", "A paleta dinâmica acompanha o papel de parede em Android 12 ou superior." to "The dynamic palette follows your wallpaper on Android 12 or later.",
        "primary • secondary • tertiary • surface • error" to "primary • secondary • tertiary • surface • error",
        "Comece pelo seu projeto" to "Start with your project", "Este exemplo é um ponto de partida — personalize tipografia, cores, formas e componentes para o seu produto." to "This example is a starting point—customize typography, colors, shapes, and components for your product.",
        "1. Abra a pasta android no Android Studio" to "1. Open the android folder in Android Studio", "2. Aguarde a sincronização do Gradle" to "2. Wait for Gradle sync",
        "3. Rode a configuração app em um dispositivo" to "3. Run the app configuration on a device", "4. Para usar no seu produto, leve os tokens e componentes com revisão de acessibilidade" to "4. To use in your product, bring the tokens and components and review accessibility",
        "No web, use o README e os exemplos na pasta web/." to "On the web, use the README and examples in the web/ folder.",
        "Ação de exemplo concluída" to "Example action completed", "Um diálogo Material 3" to "A Material 3 dialog",
        "Use diálogos para escolhas importantes e mantenha o conteúdo direto." to "Use dialogs for important choices and keep the content concise.", "Entendi" to "Got it", "Fechar" to "Close",
        "Automático usa o idioma preferido do aparelho; a escolha manual fica salva neste app." to "Automatic uses your device language; your manual choice is saved in this app.", "Idioma" to "Language", "Idioma do app" to "App language", "A paleta dinâmica acompanha o papel de parede em Android 12 ou superior." to "The dynamic palette follows your wallpaper on Android 12 or later.", "Automático (sistema)" to "Automatic (system)", "Português" to "Português", "English" to "English", "Español" to "Español",
        "Automático usa o idioma preferido do aparelho; a escolha manual fica salva neste app." to "Automatic uses your device language; your manual choice is saved in this app.",
        "Uma superfície para seu conteúdo" to "A surface for your content", "O tema claro/escuro segue a preferência do sistema." to "Light/dark theme follows system preference.",
        "Idioma" to "Language", "Idioma do app" to "App language"
    )
    val spanish = mapOf(
        "Componentes" to "Componentes", "Personalizar" to "Personalizar", "Começar" to "Empezar", "Ver diálogo" to "Ver diálogo",
        "MATERIAL 3 • COMPOSE" to "MATERIAL 3 • COMPOSE", "Design que responde a você." to "Diseño que responde a ti.",
        "A paleta dinâmica acompanha o papel de parede em Android 12 ou superior." to "La paleta dinámica sigue el fondo de pantalla en Android 12 o posterior.",
        "Um bom começo" to "Un buen comienzo", "Cores semânticas, hierarquia e controles acessíveis." to "Colores semánticos, jerarquía y controles accesibles.",
        "Testar ação" to "Probar acción", "Controles" to "Controles", "Notificações" to "Notificaciones", "Intensidade" to "Intensidad",
        "Filtros" to "Filtros", "Tudo" to "Todo", "Design" to "Diseño", "Android" to "Android", "Progresso" to "Progreso",
        "68% concluído" to "68% completado", "Ação secundária" to "Acción secundaria", "Listas e superfícies continuam legíveis em temas dinâmicos." to "Las listas y superficies siguen siendo legibles con temas dinámicos.",
        "O tema claro/escuro segue a preferência do sistema." to "El tema claro/oscuro sigue la preferencia del sistema.", "Personalização" to "Personalización",
        "Cores do sistema" to "Colores del sistema", "Dynamic Color está ativo: o app usa as cores fornecidas pelo sistema." to "Dynamic Color está activo: la app usa los colores del sistema.",
        "Este Android não fornece Dynamic Color; o app usa a paleta Material de fallback." to "Esta versión de Android no ofrece Dynamic Color; la app usa la paleta Material alternativa.",
        "Versão Android: ${Build.VERSION.SDK_INT}" to "Versión de Android: ${Build.VERSION.SDK_INT}", "Papéis semânticos" to "Roles semánticos", "A paleta dinâmica acompanha o papel de parede em Android 12 ou superior." to "La paleta dinámica sigue el fondo de pantalla en Android 12 o posterior.",
        "primary • secondary • tertiary • surface • error" to "primary • secondary • tertiary • surface • error",
        "Comece pelo seu projeto" to "Empieza con tu proyecto", "Este exemplo é um ponto de partida — personalize tipografia, cores, formas e componentes para o seu produto." to "Este ejemplo es un punto de partida: personaliza tipografía, colores, formas y componentes para tu producto.",
        "1. Abra a pasta android no Android Studio" to "1. Abre la carpeta android en Android Studio", "2. Aguarde a sincronização do Gradle" to "2. Espera la sincronización de Gradle",
        "3. Rode a configuração app em um dispositivo" to "3. Ejecuta la configuración app en un dispositivo", "4. Para usar no seu produto, leve os tokens e componentes com revisão de acessibilidade" to "4. Para usarlo en tu producto, lleva los tokens y componentes y revisa la accesibilidad",
        "No web, use o README e os exemplos na pasta web/." to "En la web, usa el README y los ejemplos de la carpeta web/.",
        "Ação de exemplo concluída" to "Acción de ejemplo completada", "Um diálogo Material 3" to "Un diálogo de Material 3",
        "Use diálogos para escolhas importantes e mantenha o conteúdo direto." to "Usa diálogos para decisiones importantes y mantén el contenido claro.", "Entendi" to "Entendido", "Fechar" to "Cerrar",
        "Automático usa o idioma preferido do aparelho; a escolha manual fica salva neste app." to "Automático usa el idioma preferido del dispositivo; tu elección manual se guarda en esta app.", "Idioma" to "Idioma", "Idioma do app" to "Idioma de la app", "A paleta dinâmica acompanha o papel de parede em Android 12 ou superior." to "La paleta dinámica sigue el fondo de pantalla en Android 12 o posterior.", "Automático (sistema)" to "Automático (sistema)", "Português" to "Português", "English" to "English", "Español" to "Español",
        "Uma superfície para seu conteúdo" to "Una superficie para tu contenido", "O tema claro/escuro segue a preferência do sistema." to "El tema claro/oscuro sigue la preferencia del sistema.",
        "Idioma" to "Idioma", "Idioma do app" to "Idioma de la app"
    )
    return when (language) { "en" -> english[source] ?: source; "es" -> spanish[source] ?: source; else -> source }
}
@Composable fun T(source: String, modifier: Modifier = Modifier, style: TextStyle = androidx.compose.material3.LocalTextStyle.current, color: Color = Color.Unspecified) { Text(localized(source, LocalAppLanguage.current), modifier = modifier, style = style, color = color) }


package dev.saynoob.materialyou

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ThemeMode { SYSTEM, LIGHT, DARK }

private const val PreferencesName = "material_you_preferences"
private const val ThemeModeKey = "theme_mode"
private const val DynamicColorKey = "dynamic_color"
private const val LanguageKey = "language"

class MainActivity : ComponentActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContent { MaterialYouApp() }
}
}

@Composable
private fun MaterialYouApp() {
val context = LocalContext.current
val preferences = remember(context) { context.getSharedPreferences(PreferencesName, Context.MODE_PRIVATE) }
var themeMode by rememberSaveable {
mutableStateOf(ThemeMode.entries.firstOrNull { it.name == preferences.getString(ThemeModeKey, ThemeMode.SYSTEM.name) } ?: ThemeMode.SYSTEM)
}
var useDynamicColor by rememberSaveable { mutableStateOf(preferences.getBoolean(DynamicColorKey, true)) }
var languageChoice by rememberSaveable { mutableStateOf(preferences.getString(LanguageKey, "auto") ?: "auto") }
@Suppress("DEPRECATION")
val deviceLanguage = LocalConfiguration.current.locale.language.lowercase().let {
when (it) { "pt", "es" -> it; else -> "en" }
}
val appLanguage = if (languageChoice == "auto") deviceLanguage else languageChoice

CompositionLocalProvider(LocalAppLanguage provides appLanguage) {
MaterialYouTheme(themeMode, useDynamicColor) {
StarterScreen(
themeMode = themeMode,
onThemeModeChange = {
themeMode = it
preferences.edit().putString(ThemeModeKey, it.name).apply()
},
useDynamicColor = useDynamicColor,
onDynamicColorChange = {
useDynamicColor = it
preferences.edit().putBoolean(DynamicColorKey, it).apply()
},
languageChoice = languageChoice,
onLanguageChange = {
languageChoice = it
preferences.edit().putString(LanguageKey, it).apply()
}
)
}
}
}

@Composable
private fun MaterialYouTheme(themeMode: ThemeMode, useDynamicColor: Boolean, content: @Composable () -> Unit) {
val darkTheme = when (themeMode) {
ThemeMode.SYSTEM -> isSystemInDarkTheme()
ThemeMode.LIGHT -> false
ThemeMode.DARK -> true
}
val context = LocalContext.current
val colors = when {
useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
darkTheme -> GreenDarkScheme
else -> GreenLightScheme
}
MaterialTheme(colorScheme = colors, content = content)
}

private val GreenLightScheme = lightColorScheme(
primary = androidx.compose.ui.graphics.Color(0xFF236B45), onPrimary = androidx.compose.ui.graphics.Color.White,
primaryContainer = androidx.compose.ui.graphics.Color(0xFFB7F6CA), onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF00210F),
secondary = androidx.compose.ui.graphics.Color(0xFF4E6354), onSecondary = androidx.compose.ui.graphics.Color.White,
secondaryContainer = androidx.compose.ui.graphics.Color(0xFFD0E8D4), onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF0B1F12),
tertiary = androidx.compose.ui.graphics.Color(0xFF3B6370), onTertiary = androidx.compose.ui.graphics.Color.White,
tertiaryContainer = androidx.compose.ui.graphics.Color(0xFFBEEAF8), onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFF001F27),
background = androidx.compose.ui.graphics.Color(0xFFF8FBF5), onBackground = androidx.compose.ui.graphics.Color(0xFF191C19),
surface = androidx.compose.ui.graphics.Color(0xFFF8FBF5), onSurface = androidx.compose.ui.graphics.Color(0xFF191C19),
surfaceVariant = androidx.compose.ui.graphics.Color(0xFFDCE5DB), onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF404941), outline = androidx.compose.ui.graphics.Color(0xFF707970)
)
private val GreenDarkScheme = darkColorScheme(
primary = androidx.compose.ui.graphics.Color(0xFF9BD9AA), onPrimary = androidx.compose.ui.graphics.Color(0xFF00391D),
primaryContainer = androidx.compose.ui.graphics.Color(0xFF07522E), onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFB7F6CA),
secondary = androidx.compose.ui.graphics.Color(0xFFB4CCB9), onSecondary = androidx.compose.ui.graphics.Color(0xFF203529),
secondaryContainer = androidx.compose.ui.graphics.Color(0xFF364B3C), onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFFD0E8D4),
tertiary = androidx.compose.ui.graphics.Color(0xFFA2CEDC), onTertiary = androidx.compose.ui.graphics.Color(0xFF043640),
tertiaryContainer = androidx.compose.ui.graphics.Color(0xFF214C57), onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFFBEEAF8),
background = androidx.compose.ui.graphics.Color(0xFF111411), onBackground = androidx.compose.ui.graphics.Color(0xFFE1E4DD),
surface = androidx.compose.ui.graphics.Color(0xFF111411), onSurface = androidx.compose.ui.graphics.Color(0xFFE1E4DD),
surfaceVariant = androidx.compose.ui.graphics.Color(0xFF404941), onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFC0C9C0), outline = androidx.compose.ui.graphics.Color(0xFF8A938A)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StarterScreen(
themeMode: ThemeMode, onThemeModeChange: (ThemeMode) -> Unit,
useDynamicColor: Boolean, onDynamicColorChange: (Boolean) -> Unit,
languageChoice: String, onLanguageChange: (String) -> Unit
) {
var destination by rememberSaveable { mutableIntStateOf(0) }
var selectedFilter by rememberSaveable { mutableStateOf("Todos") }
var notificationsEnabled by rememberSaveable { mutableStateOf(true) }
var intensity by rememberSaveable { mutableFloatStateOf(0.64f) }
var showDialog by rememberSaveable { mutableStateOf(false) }
val snackbarHostState = remember { SnackbarHostState() }
val scope = rememberCoroutineScope()
val language = LocalAppLanguage.current
val destinations = listOf(
Destination("Explorar", Icons.Outlined.AutoAwesome), Destination("Tema", Icons.Outlined.Palette), Destination("Sobre", Icons.Outlined.Code)
)
Scaffold(
topBar = { TopAppBar(title = { T("Material You") }, actions = {
IconButton(onClick = { onThemeModeChange(if (themeMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK) }) {
Icon(if (themeMode == ThemeMode.DARK) Icons.Outlined.LightMode else Icons.Outlined.DarkMode, localized("Alternar tema", LocalAppLanguage.current))
}
}) },
snackbarHost = { SnackbarHost(snackbarHostState) },
bottomBar = { NavigationBar { destinations.forEachIndexed { index, item ->
NavigationBarItem(selected = destination == index, onClick = { destination = index }, icon = { Icon(item.icon, null) }, label = { T(item.label) })
} } },
floatingActionButton = { ExtendedFloatingActionButton(onClick = { showDialog = true }, icon = { Icon(Icons.Outlined.AutoAwesome, null) }, text = { T("Ver diálogo") }) }
) { padding ->
when (destination) {
0 -> ExplorePage(padding, selectedFilter, { selectedFilter = it }, notificationsEnabled, { notificationsEnabled = it }, intensity, { intensity = it }) {
scope.launch { snackbarHostState.showSnackbar(localized("Ação de exemplo concluída", language)) }
}
1 -> ThemePage(padding, themeMode, onThemeModeChange, useDynamicColor, onDynamicColorChange, languageChoice, onLanguageChange)
else -> AboutPage(padding)
}
}
if (showDialog) AlertDialog(
onDismissRequest = { showDialog = false }, title = { T("Um diálogo Material 3") },
text = { T("Use diálogos para escolhas importantes e mantenha o conteúdo direto.") },
confirmButton = { TextButton(onClick = { showDialog = false }) { T("Entendi") } },
dismissButton = { TextButton(onClick = { showDialog = false }) { T("Fechar") } }
)
}
private data class Destination(val label: String, val icon: ImageVector)

@Composable
private fun ExplorePage(padding: PaddingValues, selectedFilter: String, onFilterChange: (String) -> Unit, notificationsEnabled: Boolean, onNotificationsChange: (Boolean) -> Unit, intensity: Float, onIntensityChange: (Float) -> Unit, onExampleAction: () -> Unit) {
var pressed by remember { mutableStateOf(false) }
val scope = rememberCoroutineScope()
val language = LocalAppLanguage.current
val scale by animateFloatAsState(if (pressed) .965f else 1f, spring(dampingRatio = .58f, stiffness = 420f), label = "example-card-spring")
LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
item {
T("MATERIAL 3 • COMPOSE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
Spacer(Modifier.height(8.dp)); T("Uma base nativa, clara e pronta para adaptar.", style = MaterialTheme.typography.headlineMedium)
Spacer(Modifier.height(8.dp)); T("Experimente preferências persistentes, Dynamic Color e uma paleta verde de fallback.", color = MaterialTheme.colorScheme.onSurfaceVariant)
}
item { Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale }.clickable { scope.launch { pressed = true; delay(110); pressed = false } }) {
Column(Modifier.fillMaxWidth().padding(24.dp)) { T("Feito para o sistema", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer); Spacer(Modifier.height(8.dp)); T("No Android 12+, a cor dinâmica pode seguir o papel de parede. Em outras versões, os papéis semânticos continuam consistentes.", color = MaterialTheme.colorScheme.onPrimaryContainer); Spacer(Modifier.height(14.dp)); Button(onClick = onExampleAction) { T("Testar ação") } }
} }
item { ConnectedSelectionDemo(selectedFilter = selectedFilter, onFilterChange = onFilterChange) }
item { ComponentShowcase() }
item { T("Filtros", style = MaterialTheme.typography.titleLarge) }
item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Todos", "Design", "Android").forEach { label -> FilterChip(selected = selectedFilter == label, onClick = { onFilterChange(label) }, label = { T(label) }) } } }
item { Card { Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { Column(Modifier.weight(1f)) { T("Notificações", style = MaterialTheme.typography.titleMedium); T("Exemplo de preferência de interface", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall) }; Spacer(Modifier.width(12.dp)); Switch(notificationsEnabled, onNotificationsChange) }
Text("${localized("Intensidade", LocalAppLanguage.current)} · ${(intensity * 100).toInt()}%", style = MaterialTheme.typography.titleMedium); Slider(intensity, onIntensityChange)
} } }
item { Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) { ListItem(headlineContent = { T("Papéis semânticos") }, supportingContent = { T("Use primary, containers e superfícies no lugar de cores soltas.") }, leadingContent = { Icon(Icons.Outlined.SettingsSuggest, null) }, colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) } }
}
}

@Composable
private fun ThemePage(padding: PaddingValues, themeMode: ThemeMode, onThemeModeChange: (ThemeMode) -> Unit, useDynamicColor: Boolean, onDynamicColorChange: (Boolean) -> Unit, languageChoice: String, onLanguageChange: (String) -> Unit) {
val dynamicAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
var languageExpanded by remember { mutableStateOf(false) }
LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
item { T("Personalize", style = MaterialTheme.typography.headlineMedium); Spacer(Modifier.height(8.dp)); T("As preferências abaixo são salvas no aparelho e sobrevivem ao reinício do app.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
item { Card { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { T("Tema", style = MaterialTheme.typography.titleLarge); ThemeMode.entries.forEach { mode -> FilterChip(selected = themeMode == mode, onClick = { onThemeModeChange(mode) }, label = { T(when (mode) { ThemeMode.SYSTEM -> "Sistema"; ThemeMode.LIGHT -> "Claro"; ThemeMode.DARK -> "Escuro" }) }, leadingIcon = if (themeMode == mode) {{ Icon(Icons.Outlined.WbSunny, null) }} else null) } } } }
item { Card { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { T("Idioma do app", style = MaterialTheme.typography.titleLarge); Box { OutlinedButton(onClick = { languageExpanded = true }) { T(languageLabel(languageChoice)) }; DropdownMenu(languageExpanded, { languageExpanded = false }) { listOf("auto" to "Automático (sistema)", "pt" to "Português", "en" to "English", "es" to "Español").forEach { (value, label) -> DropdownMenuItem(text = { T(label) }, onClick = { onLanguageChange(value); languageExpanded = false }) } } }; T("Automático usa o idioma preferido do aparelho; a escolha manual fica salva neste app.", color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
item { Card { ListItem(headlineContent = { T("Cor dinâmica") }, supportingContent = { T(if (dynamicAvailable) "Usa as cores do sistema no Android 12 ou superior." else "Indisponível neste Android; a paleta verde de fallback está em uso.") }, trailingContent = { Switch(useDynamicColor, onDynamicColorChange, enabled = dynamicAvailable) }) } }
item { Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) { Column(Modifier.padding(20.dp)) { T("Fallback verde", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer); Spacer(Modifier.height(8.dp)); T("Uma paleta clara e uma escura estão definidas para dispositivos antigos ou quando a cor dinâmica é desligada.", color = MaterialTheme.colorScheme.onSecondaryContainer) } } }
}
}
private fun languageLabel(choice: String) = when (choice) { "pt" -> "Português"; "en" -> "English"; "es" -> "Español"; else -> "Automático (sistema)" }

@Composable
private fun AboutPage(padding: PaddingValues) {
LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
item { T("Comece pelo seu projeto", style = MaterialTheme.typography.headlineMedium) }
item { T("Esta demonstração usa Material 3 estável, Compose e cores semânticas. Copie a estrutura, não apenas os pixels.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
item { Card { ListItem(headlineContent = { T("1. Escolha um tema") }, supportingContent = { T("Sistema, claro ou escuro, sempre com fallback consistente.") }, leadingContent = { Icon(Icons.Outlined.Palette, null) }) } }
item { Card { ListItem(headlineContent = { T("2. Revise acessibilidade") }, supportingContent = { T("Valide contraste, foco, toque e leitor de tela no produto final.") }, leadingContent = { Icon(Icons.Outlined.AutoAwesome, null) }) } }
}
}

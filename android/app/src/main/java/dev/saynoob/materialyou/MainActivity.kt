package dev.saynoob.materialyou

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ListItem
import androidx.compose.runtime.*
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.isSystemInDarkTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { StarterTheme { StarterApp() } }
    }
}

@Composable
private fun StarterTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val context = LocalContext.current
    val dynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colors = when {
        dynamic && dark -> dynamicDarkColorScheme(context)
        dynamic -> dynamicLightColorScheme(context)
        dark -> darkColorScheme()
        else -> lightColorScheme()
    }
    MaterialTheme(colorScheme = colors, content = content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StarterApp() {
    val context = LocalContext.current
    var languageChoice by remember { mutableStateOf(context.getSharedPreferences("starter", 0).getString("language", "auto") ?: "auto") }
    val systemLanguage = LocalConfiguration.current.locale.language.lowercase().let { if (it.startsWith("es")) "es" else if (it.startsWith("en")) "en" else "pt" }
    val appLanguage = if (languageChoice == "auto") systemLanguage else languageChoice
    var destination by remember { mutableStateOf(0) }
    var selectedFilter by remember { mutableStateOf("Tudo") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var intensity by remember { mutableFloatStateOf(.64f) }
    var showDialog by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val pages = listOf("Componentes", "Personalizar", "Começar")

    CompositionLocalProvider(LocalAppLanguage provides appLanguage) { Scaffold(
        topBar = { TopAppBar(title = { T("Material You Starter") }) },
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            NavigationBar {
                pages.forEachIndexed { index, rawLabel ->
                    val label = localized(rawLabel, appLanguage)
                    NavigationBarItem(
                        selected = destination == index,
                        onClick = { destination = index },
                        icon = { Text(listOf("◈", "◉", "⌘")[index]) },
                        label = { T(label) }
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { showDialog = true }, text = { T("Ver diálogo") }, icon = { T("＋") })
        }
    ) { padding ->
        when (destination) {
            0 -> ComponentsPage(
                padding = padding,
                selectedFilter = selectedFilter,
                onFilter = { selectedFilter = it },
                notificationsEnabled = notificationsEnabled,
                onNotifications = { notificationsEnabled = it },
                intensity = intensity,
                onIntensity = { intensity = it },
                onSnackbar = { scope.launch { snackbar.showSnackbar(localized("Ação de exemplo concluída", appLanguage)) } }
            )
            1 -> PersonalizePage(padding, languageChoice, { choice -> languageChoice = choice; context.getSharedPreferences("starter", 0).edit().putString("language", choice).apply() })
            else -> GetStartedPage(padding)
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { T("Um diálogo Material 3") },
            text = { T("Use diálogos para escolhas importantes e mantenha o conteúdo direto.") },
            confirmButton = { TextButton(onClick = { showDialog = false }) { T("Entendi") } },
            dismissButton = { TextButton(onClick = { showDialog = false }) { T("Fechar") } }
        )
    }
    }
}

@Composable
private fun ComponentsPage(
    padding: PaddingValues,
    selectedFilter: String,
    onFilter: (String) -> Unit,
    notificationsEnabled: Boolean,
    onNotifications: (Boolean) -> Unit,
    intensity: Float,
    onIntensity: (Float) -> Unit,
    onSnackbar: () -> Unit
) {
    var springCardPressed by remember { mutableStateOf(false) }
    val springScope = rememberCoroutineScope()
    val springScale by animateFloatAsState(
        targetValue = if (springCardPressed) 0.965f else 1f,
        animationSpec = spring(dampingRatio = 0.58f, stiffness = 420f),
        label = "prismal-inspired-spring"
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            T("MATERIAL 3 • COMPOSE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            T("Design que responde a você.", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(6.dp))
            T("A paleta dinâmica acompanha o papel de parede em Android 12 ou superior.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Card(shape = RoundedCornerShape(28.dp), modifier = Modifier.graphicsLayer { scaleX = springScale; scaleY = springScale }.clickable { springScope.launch { springCardPressed = true; delay(110); springCardPressed = false } }, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.fillMaxWidth().padding(22.dp)) {
                    T("Um bom começo", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(Modifier.height(5.dp))
                    T("Cores semânticas, hierarquia e controles acessíveis.", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(Modifier.height(14.dp))
                    Button(onClick = onSnackbar) { T("Testar ação") }
                }
            }
        }
        item { T("Controles", style = MaterialTheme.typography.titleLarge) }
        item {
            Card {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        T("Notificações", style = MaterialTheme.typography.bodyLarge)
                        Switch(checked = notificationsEnabled, onCheckedChange = onNotifications)
                    }
                    T("${localized("Intensidade", LocalAppLanguage.current)} · ${(intensity * 100).toInt()}%")
                    Slider(value = intensity, onValueChange = onIntensity)
                }
            }
        }
        item { T("Filtros", style = MaterialTheme.typography.titleLarge) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Tudo", "Design", "Android").forEach { rawLabel ->
                    FilterChip(selected = selectedFilter == rawLabel, onClick = { onFilter(rawLabel) }, label = { T(rawLabel) })
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(24.dp)) {
                Column(Modifier.fillMaxWidth().padding(18.dp)) {
                    T("Progresso", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(progress = { .68f }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(6.dp))
                    T("68% concluído", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                ListItem(
                    headlineContent = { T("Ação secundária") },
                    supportingContent = { T("Listas e superfícies continuam legíveis em temas dinâmicos.") },
                    leadingContent = { T("✦", color = MaterialTheme.colorScheme.onTertiaryContainer) },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                )
            }
        }
        item { T("O tema claro/escuro segue a preferência do sistema.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable
private fun PersonalizePage(padding: PaddingValues, languageChoice: String, onLanguageChange: (String) -> Unit) {
    val dynamicAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text(localized("Personalização", LocalAppLanguage.current), style = MaterialTheme.typography.headlineMedium) }
        item {
            var expanded by remember { mutableStateOf(false) }
            Card {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    T("Idioma do app")
                    Box {
                        OutlinedButton(onClick = { expanded = true }) { T(when (languageChoice) { "en" -> "English"; "es" -> "Español"; else -> "Automático (sistema)" }) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("auto" to "Automático (sistema)", "pt" to "Português", "en" to "English", "es" to "Español").forEach { (value, label) ->
                                DropdownMenuItem(text = { T(label) }, onClick = { onLanguageChange(value); expanded = false })
                            }
                        }
                    }
                    T("Automático usa o idioma preferido do aparelho; a escolha manual fica salva neste app.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        item {
            Card {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    T("Cores do sistema", style = MaterialTheme.typography.titleLarge)
                    T(if (dynamicAvailable) "Dynamic Color está ativo: o app usa as cores fornecidas pelo sistema." else "Este Android não fornece Dynamic Color; o app usa a paleta Material de fallback.")
                    T("Versão Android: ${Build.VERSION.SDK_INT}", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(20.dp)) {
                    T("Papéis semânticos", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Spacer(Modifier.height(8.dp))
                    T("primary • secondary • tertiary • surface • error", color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
        }
    }
}

@Composable
private fun GetStartedPage(padding: PaddingValues) {
    val steps = listOf("1. Abra a pasta android no Android Studio", "2. Aguarde a sincronização do Gradle", "3. Rode a configuração app em um dispositivo", "4. Para usar no seu produto, leve os tokens e componentes com revisão de acessibilidade")
    LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { T("Comece pelo seu projeto", style = MaterialTheme.typography.headlineMedium) }
        item { T("Este exemplo é um ponto de partida — personalize tipografia, cores, formas e componentes para o seu produto.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        items(steps) { step ->
            Card {
                ListItem(headlineContent = { T(step) }, leadingContent = { T("✓", color = MaterialTheme.colorScheme.primary) })
            }
        }
        item { T("No web, use o README e os exemplos na pasta web/.", style = MaterialTheme.typography.bodyMedium) }
    }
}

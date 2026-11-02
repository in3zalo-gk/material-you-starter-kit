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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.isSystemInDarkTheme
import kotlinx.coroutines.launch

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
    var destination by remember { mutableStateOf(0) }
    var selectedFilter by remember { mutableStateOf("Tudo") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var intensity by remember { mutableFloatStateOf(.64f) }
    var showDialog by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val pages = listOf("Componentes", "Personalizar", "Começar")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Material You Starter") }) },
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            NavigationBar {
                pages.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = destination == index,
                        onClick = { destination = index },
                        icon = { Text(listOf("◈", "◉", "⌘")[index]) },
                        label = { Text(label) }
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { showDialog = true }, text = { Text("Ver diálogo") }, icon = { Text("＋") })
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
                onSnackbar = { scope.launch { snackbar.showSnackbar("Ação de exemplo concluída") } }
            )
            1 -> PersonalizePage(padding)
            else -> GetStartedPage(padding)
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Um diálogo Material 3") },
            text = { Text("Use diálogos para escolhas importantes e mantenha o conteúdo direto.") },
            confirmButton = { TextButton(onClick = { showDialog = false }) { Text("Entendi") } },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Fechar") } }
        )
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
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("MATERIAL 3 • COMPOSE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text("Design que responde a você.", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(6.dp))
            Text("A paleta dinâmica acompanha o papel de parede em Android 12 ou superior.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.fillMaxWidth().padding(22.dp)) {
                    Text("Um bom começo", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(Modifier.height(5.dp))
                    Text("Cores semânticas, hierarquia e controles acessíveis.", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(Modifier.height(14.dp))
                    Button(onClick = onSnackbar) { Text("Testar ação") }
                }
            }
        }
        item { Text("Controles", style = MaterialTheme.typography.titleLarge) }
        item {
            Card {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Notificações", style = MaterialTheme.typography.bodyLarge)
                        Switch(checked = notificationsEnabled, onCheckedChange = onNotifications)
                    }
                    Text("Intensidade · ${(intensity * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium)
                    Slider(value = intensity, onValueChange = onIntensity)
                }
            }
        }
        item { Text("Filtros", style = MaterialTheme.typography.titleLarge) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Tudo", "Design", "Android").forEach { label ->
                    FilterChip(selected = selectedFilter == label, onClick = { onFilter(label) }, label = { Text(label) })
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(24.dp)) {
                Column(Modifier.fillMaxWidth().padding(18.dp)) {
                    Text("Progresso", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(progress = { .68f }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(6.dp))
                    Text("68% concluído", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                ListItem(
                    headlineContent = { Text("Ação secundária") },
                    supportingContent = { Text("Listas e superfícies continuam legíveis em temas dinâmicos.") },
                    leadingContent = { Text("✦", color = MaterialTheme.colorScheme.onTertiaryContainer) },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                )
            }
        }
        item { Text("O tema claro/escuro segue a preferência do sistema.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable
private fun PersonalizePage(padding: PaddingValues) {
    val dynamicAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text("Personalização", style = MaterialTheme.typography.headlineMedium) }
        item {
            Card {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Cores do sistema", style = MaterialTheme.typography.titleLarge)
                    Text(if (dynamicAvailable) "Dynamic Color está ativo: o app usa as cores fornecidas pelo sistema." else "Este Android não fornece Dynamic Color; o app usa a paleta Material de fallback.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Versão Android: ${Build.VERSION.SDK_INT}", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(20.dp)) {
                    Text("Papéis semânticos", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Spacer(Modifier.height(8.dp))
                    Text("primary • secondary • tertiary • surface • error", color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
        }
    }
}

@Composable
private fun GetStartedPage(padding: PaddingValues) {
    val steps = listOf("1. Abra a pasta android no Android Studio", "2. Aguarde a sincronização do Gradle", "3. Rode a configuração app em um dispositivo", "4. Para usar no seu produto, leve os tokens e componentes com revisão de acessibilidade")
    LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text("Comece pelo seu projeto", style = MaterialTheme.typography.headlineMedium) }
        item { Text("Este exemplo é um ponto de partida — personalize tipografia, cores, formas e componentes para o seu produto.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        items(steps) { step ->
            Card {
                ListItem(headlineContent = { Text(step) }, leadingContent = { Text("✓", color = MaterialTheme.colorScheme.primary) })
            }
        }
        item { Text("No web, use o README e os exemplos na pasta web/.", style = MaterialTheme.typography.bodyMedium) }
    }
}

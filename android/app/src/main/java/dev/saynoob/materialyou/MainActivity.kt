package dev.saynoob.materialyou

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.isSystemInDarkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { StarterTheme { StarterScreen() } }
    }
}

@Composable
private fun StarterTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val context = LocalContext.current
    val dynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val scheme = when {
        dynamic && dark -> dynamicDarkColorScheme(context)
        dynamic -> dynamicLightColorScheme(context)
        dark -> darkColorScheme()
        else -> lightColorScheme()
    }
    MaterialTheme(colorScheme = scheme, content = content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StarterScreen() {
    var selected by remember { mutableStateOf("Tudo") }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Material You Starter") }) },
        floatingActionButton = { ExtendedFloatingActionButton(onClick = { }) { Text("Criar projeto") } }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("DESIGN SYSTEM PARA COMEÇAR", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
                Text("Uma interface que se adapta a você.", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                Text("Exemplo Material 3 em Jetpack Compose, com Dynamic Color quando disponível.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            item {
                Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(22.dp)) {
                        Text("Seu próximo projeto começa aqui", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(Modifier.height(8.dp))
                        Text("Use tokens semânticos e componentes acessíveis como ponto de partida.", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { }) { Text("Começar agora") }
                    }
                }
            }
            item { Text("Seus espaços", style = MaterialTheme.typography.titleLarge) }
            item {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Design System", style = MaterialTheme.typography.titleMedium)
                            Text("Tokens e componentes", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(12.dp))
                            LinearProgressIndicator(progress = { .68f }, modifier = Modifier.fillMaxWidth())
                            Text("68% concluído", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Tudo", "Design", "Android").forEach { label ->
                        FilterChip(selected = selected == label, onClick = { selected = label }, label = { Text(label) })
                    }
                }
            }
            item { Text("Tema do sistema aplicado automaticamente", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

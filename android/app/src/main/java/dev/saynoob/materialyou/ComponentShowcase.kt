package dev.saynoob.materialyou

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.util.Date
import java.util.TimeZone

/**
 * Local-only Material 3 component samples. Their state is intentionally kept in memory:
 * the starter does not imply a scheduling or data persistence feature.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ComponentShowcase() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var selectedMenuItem by rememberSaveable { mutableStateOf("Prioridade normal") }
    var menuExpanded by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }
    var details by rememberSaveable { mutableStateOf("") }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var showSheet by rememberSaveable { mutableStateOf(false) }
    var selectedDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedHour by rememberSaveable { mutableIntStateOf(9) }
    var selectedMinute by rememberSaveable { mutableIntStateOf(30) }
    val language = LocalAppLanguage.current
    val tabs = listOf("Formulário", "Lista")

    Card {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(Modifier.padding(horizontal = 20.dp)) {
                T("Explore componentes", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                T(
                    "Exemplos locais com estado em memória para experimentar componentes Material 3.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            PrimaryTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, label ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { T(label) }
                    )
                }
            }

            when (selectedTab) {
                0 -> FormTab(
                    title = title,
                    onTitleChange = { title = it },
                    details = details,
                    onDetailsChange = { details = it },
                    selectedMenuItem = selectedMenuItem,
                    menuExpanded = menuExpanded,
                    onMenuExpandedChange = { menuExpanded = it },
                    onMenuItemSelected = {
                        selectedMenuItem = it
                        menuExpanded = false
                    },
                    dateLabel = selectedDateMillis?.let { formatDate(it, language) } ?: localized("Escolher data", language),
                    timeLabel = "%02d:%02d".format(selectedHour, selectedMinute),
                    onDateClick = { showDatePicker = true },
                    onTimeClick = { showTimePicker = true },
                    onSheetClick = { showSheet = true }
                )
                else -> ListTab(selectedMenuItem)
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) { T("Confirmar") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { T("Cancelar") } }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { T("Escolher horário") },
            text = { TimePicker(state = timePickerState) },
            confirmButton = {
                TextButton(onClick = {
                    selectedHour = timePickerState.hour
                    selectedMinute = timePickerState.minute
                    showTimePicker = false
                }) { T("Confirmar") }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { T("Cancelar") } }
        )
    }

    if (showSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                T("Ações rápidas", style = MaterialTheme.typography.titleLarge)
                T("Bottom sheet modal para ações contextuais sem trocar de tela.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                HorizontalDivider()
                TextButton(onClick = { showSheet = false }, modifier = Modifier.align(Alignment.End)) { T("Fechar") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormTab(
    title: String,
    onTitleChange: (String) -> Unit,
    details: String,
    onDetailsChange: (String) -> Unit,
    selectedMenuItem: String,
    menuExpanded: Boolean,
    onMenuExpandedChange: (Boolean) -> Unit,
    onMenuItemSelected: (String) -> Unit,
    dateLabel: String,
    timeLabel: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onSheetClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { T("Título") },
            supportingText = { T("Campo de texto com rótulo persistente.") }
        )
        OutlinedTextField(
            value = details,
            onValueChange = onDetailsChange,
            modifier = Modifier.fillMaxWidth(),
            label = { T("Detalhes") },
            minLines = 2
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = { onMenuExpandedChange(true) }) { T(selectedMenuItem) }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { onMenuExpandedChange(false) }) {
                listOf("Prioridade baixa", "Prioridade normal", "Prioridade alta").forEach { item ->
                    DropdownMenuItem(text = { T(item) }, onClick = { onMenuItemSelected(item) })
                }
            }
            Spacer(Modifier.width(8.dp))
            T("Menu", style = MaterialTheme.typography.labelLarge)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { T("Abre o seletor de data") } },
                state = rememberTooltipState()
            ) { OutlinedButton(onClick = onDateClick) { T(dateLabel) } }
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { T("Abre o seletor de horário") } },
                state = rememberTooltipState()
            ) { OutlinedButton(onClick = onTimeClick) { T(timeLabel) } }
        }
        Button(onClick = onSheetClick, modifier = Modifier.fillMaxWidth()) { T("Abrir ações rápidas") }
    }
}

@Composable
private fun ListTab(selectedMenuItem: String) {
    Column(Modifier.fillMaxWidth()) {
        T("Itens de exemplo", modifier = Modifier.padding(horizontal = 20.dp), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        listOf("Planejar interface", "Revisar contraste", "Testar toque").forEachIndexed { index, item ->
            ListItem(
                headlineContent = { T(item) },
                supportingContent = { T(if (index == 0) selectedMenuItem else "Estado apenas visual") },
                trailingContent = {
                    FilterChip(selected = index == 0, onClick = {}, label = { T(if (index == 0) "Ativo" else "Pendente") })
                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    }
}

private fun formatDate(millis: Long, language: String): String {
    val locale = when (language) {
        "en" -> java.util.Locale.ENGLISH
        "es" -> java.util.Locale("es")
        else -> java.util.Locale("pt", "BR")
    }
    return DateFormat.getDateInstance(DateFormat.MEDIUM, locale).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date(millis))
}

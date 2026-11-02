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

@Composable
internal fun ConnectedSelectionDemo(selectedFilter: String, onFilterChange: (String) -> Unit) {
    val options = listOf("Todos", "Design", "Android")
    val selectedIndex = options.indexOf(selectedFilter).coerceAtLeast(0)
    val shape = RoundedCornerShape(24.dp)
    Card {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            T("Seleção conectada", style = MaterialTheme.typography.titleLarge)
            T(
                "Toque uma opção: o indicador preenchido acompanha a seleção conectada e cada botão usa o ripple nativo.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val segmentWidth: Dp = maxWidth / options.size
                // Compose animation clocks honor Android's Animator duration scale.
                val indicatorOffset by animateDpAsState(
                    targetValue = segmentWidth * selectedIndex,
                    animationSpec = tween(durationMillis = 280),
                    label = "connected-selection-indicator"
                )
                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .width(segmentWidth)
                        .height(48.dp)
                        .padding(4.dp)
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Row(Modifier.fillMaxWidth().selectableGroup()) {
                    options.forEachIndexed { index, option ->
                        val selected = index == selectedIndex
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                                .selectable(
                                    selected = selected,
                                    onClick = { onFilterChange(option) },
                                    role = Role.RadioButton
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            T(
                                option,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}


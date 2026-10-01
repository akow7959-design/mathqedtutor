package com.example.ui.whiteboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintSecondary

@Composable
fun WhiteboardToolbar(
    modifier: Modifier = Modifier,
    currentTool: WhiteboardTool,
    onToolSelected: (WhiteboardTool) -> Unit,
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    isAutoFixEnabled: Boolean,
    onToggleAutoFix: (Boolean) -> Unit,
    showGrid: Boolean,
    onToggleGrid: (Boolean) -> Unit,
    onUndo: () -> Unit,
    onClear: () -> Unit,
    onAddFormula: (String) -> Unit
) {
    var showFormulaDialog by remember { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFF161A2E), // Slate dark
        Color(0xFF5B6CF9), // Electric indigo
        Color(0xFF19C39C), // Mint green
        Color(0xFFFF7A85), // Coral rose
        Color(0xFFFFB020)  // Amber
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF9FAFF),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E6F5)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Tools row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ToolButton(
                    icon = Icons.Default.Edit,
                    label = "Қалам",
                    isSelected = currentTool == WhiteboardTool.PEN,
                    onClick = { onToolSelected(WhiteboardTool.PEN) },
                    testTag = "tool_pen"
                )
                ToolButton(
                    icon = Icons.Default.HorizontalRule,
                    label = "Сызық",
                    isSelected = currentTool == WhiteboardTool.LINE,
                    onClick = { onToolSelected(WhiteboardTool.LINE) },
                    testTag = "tool_line"
                )
                ToolButton(
                    icon = Icons.Default.CropSquare,
                    label = "Төртбұрыш",
                    isSelected = currentTool == WhiteboardTool.RECTANGLE,
                    onClick = { onToolSelected(WhiteboardTool.RECTANGLE) },
                    testTag = "tool_rect"
                )
                ToolButton(
                    icon = Icons.Default.Circle,
                    label = "Шеңбер",
                    isSelected = currentTool == WhiteboardTool.CIRCLE,
                    onClick = { onToolSelected(WhiteboardTool.CIRCLE) },
                    testTag = "tool_circle"
                )
                ToolButton(
                    icon = Icons.Default.ChangeHistory,
                    label = "Үшбұрыш",
                    isSelected = currentTool == WhiteboardTool.TRIANGLE,
                    onClick = { onToolSelected(WhiteboardTool.TRIANGLE) },
                    testTag = "tool_triangle"
                )
                ToolButton(
                    icon = Icons.Default.Functions,
                    label = "Формула",
                    isSelected = false,
                    onClick = { showFormulaDialog = true },
                    testTag = "tool_formula"
                )
                ToolButton(
                    icon = Icons.Default.DeleteSweep,
                    label = "Өшіргіш",
                    isSelected = currentTool == WhiteboardTool.ERASER,
                    onClick = { onToolSelected(WhiteboardTool.ERASER) },
                    testTag = "tool_eraser"
                )

                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(28.dp)
                        .background(Color(0xFFD9DDF3))
                )
                Spacer(modifier = Modifier.width(6.dp))

                // Color chips
                colors.forEach { col ->
                    val isSelected = currentColor == col
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(col)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) Color.White else Color(0x33000000),
                                shape = CircleShape
                            )
                            .clickable { onColorSelected(col) }
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(28.dp)
                        .background(Color(0xFFD9DDF3))
                )
                Spacer(modifier = Modifier.width(6.dp))

                // Action icons
                IconButton(
                    onClick = onUndo,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Undo",
                        tint = Color(0xFF4A5578),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { onToggleGrid(!showGrid) },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = "Grid",
                        tint = if (showGrid) IndigoPrimary else Color(0xFF4A5578),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear",
                        tint = Color(0xFFFF5C72),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Sub-row: Auto-recognition toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = null,
                        tint = if (isAutoFixEnabled) MintSecondary else Color(0xFF8C95B5),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Фигураны тану (Auto-snap):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4A5578)
                    )
                }

                Switch(
                    checked = isAutoFixEnabled,
                    onCheckedChange = onToggleAutoFix,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MintSecondary
                    ),
                    modifier = Modifier.size(width = 38.dp, height = 24.dp)
                )
            }
        }
    }

    if (showFormulaDialog) {
        FormulaPickerDialog(
            onDismiss = { showFormulaDialog = false },
            onSelectFormula = { formula ->
                onAddFormula(formula)
                showFormulaDialog = false
            }
        )
    }
}

@Composable
private fun ToolButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        color = if (isSelected) IndigoPrimary else Color.Transparent,
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.White else Color(0xFF4A5578),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun FormulaPickerDialog(
    onDismiss: () -> Unit,
    onSelectFormula: (String) -> Unit
) {
    var customText by remember { mutableStateOf("") }
    val presetFormulas = listOf(
        "x = (-b ± √D) / 2a",
        "D = b² - 4ac",
        "x₁ + x₂ = -p,  x₁·x₂ = q",
        "(u · v)' = u'v + uv'",
        "(xⁿ)' = n · xⁿ⁻¹",
        "c² = a² + b² (Пифагор)",
        "aₙ = a₁ + (n - 1)d",
        "S = ½ · a · h",
        "sin²α + cos²α = 1"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Формула стикерін қосу",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Дайын формулалар:",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    fontWeight = FontWeight.SemiBold
                )
                presetFormulas.take(5).forEach { f ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectFormula(f) },
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF3F5FD),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD8DEFB))
                    ) {
                        Text(
                            text = f,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = IndigoPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = customText,
                    onValueChange = { customText = it },
                    label = { Text("Өз формулаңыз") },
                    placeholder = { Text("Мысалы: f'(x) = 2x") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (customText.isNotBlank()) onSelectFormula(customText)
                },
                enabled = customText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                Text("Қою")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Жабу")
            }
        }
    )
}

package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.LessonTask
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintSecondary
import com.example.ui.viewmodel.ScreenDestination
import com.example.ui.viewmodel.VimboxViewModel

@Composable
fun GeneratorScreen(
    viewModel: VimboxViewModel
) {
    BackHandler {
        viewModel.setScreen(ScreenDestination.DASHBOARD)
    }

    val selectedFormat by viewModel.generatorFormat.collectAsStateWithLifecycle()
    val parsedTasks by viewModel.parsedTasks.collectAsStateWithLifecycle()
    val isParsing by viewModel.isParsing.collectAsStateWithLifecycle()

    var rawInputText by remember {
        mutableStateOf(
            """1. x^2 - 5x + 6 = 0 теңдеуінің түбірлерінің көбейтіндісін табыңыз.
A) 2
B) 3
C) 6
D) -6
E) 5

2. sqrt(64) + 4^2 өрнегінің мәнін есептеңіз.
A) 24
B) 20
C) 16
D) 22
E) 28

3. y = 4x^3 - 6x + 5 функциясының туындысын табыңыз.
A) 12x^2 - 6
B) 12x^2
C) 4x^2 - 6
D) 12x - 6
E) 12x^3 - 6"""
        )
    }

    val formats = listOf(
        "ENT" to "🎯 ҰБТ (5 нұсқа A-E)",
        "NIS" to "🏛 НИШ (4 нұсқа A-D)",
        "BIL" to "🔷 БИЛ (4 нұсқа)",
        "OPEN" to "✍️ Ашық жауап"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5FD))
            .padding(16.dp)
            .testTag("generator_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Header
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(IndigoPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "PDF & Мәтін Тест Генераторы",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp,
                        color = Color(0xFF161A2E)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Құжат мәтінін немесе есептерді көшіріп қойыңыз — автоматты түрде LaTeX формулалары мен таза жауап нұсқаларына түседі.",
                    fontSize = 13.sp,
                    color = Color(0xFF767D9C)
                )
            }
        }

        // Format Selector
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ЕМТИХАН ФОРМАТЫ:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B7280),
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        formats.forEach { (code, label) ->
                            val isSelected = selectedFormat == code
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { viewModel.setGeneratorFormat(code) },
                                color = if (isSelected) IndigoPrimary else Color(0xFFF3F5FD),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) IndigoPrimary else Color(0xFFDCE2F8)
                                )
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color.White else Color(0xFF1E243A),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.5.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Raw Text Input & Preset Buttons
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ТАПСЫРМАЛАР МӘТІНІ:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B7280),
                            letterSpacing = 0.5.sp
                        )

                        // Demo Loader buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        rawInputText = """1. 3, 9, 27, 81 қатарының 5-ші мүшесін табыңыз.
A) 162
B) 243
C) 324
D) 216

2. Егер a + b = 10 және ab = 21 болса, a^2 + b^2 табыңыз.
A) 58
B) 62
C) 79
D) 100"""
                                    },
                                color = Color(0xFFEEF0FF)
                            ) {
                                Text(
                                    text = "Демо НИШ",
                                    color = IndigoPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rawInputText,
                        onValueChange = { rawInputText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .testTag("generator_input_field"),
                        shape = RoundedCornerShape(14.dp),
                        placeholder = { Text("1. x^2 - 5x + 6 = 0...\nA) ... B) ...") }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.parseInputText(rawInputText) },
                        enabled = rawInputText.isNotBlank() && !isParsing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("parse_text_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        if (isParsing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Тану жүріп жатыр...")
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Тестке айналдыру және LaTeX жасау ✨", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Parsed Results
        if (parsedTasks.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ТАНЫЛҒАН ТАПСЫРМАЛАР (${parsedTasks.size}):",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B7280),
                        letterSpacing = 0.5.sp
                    )

                    Button(
                        onClick = { viewModel.saveParsedTasksToClassroom() },
                        colors = ButtonDefaults.buttonColors(containerColor = MintSecondary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("save_to_classroom_btn")
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Vimbox аудиториясына жүктеу", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            items(parsedTasks) { task ->
                ParsedTaskCard(task = task)
            }
        }
    }
}

@Composable
private fun ParsedTaskCard(task: LessonTask) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(IndigoPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${task.orderNo}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MintContainer
                ) {
                    Text(
                        text = "LaTeX дайын ✓",
                        color = Color(0xFF065F46),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = task.question,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.5.sp,
                color = Color(0xFF161A2E),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Options list
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                task.getOptionsList().forEach { opt ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF9FAFE),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = opt,
                            fontSize = 13.sp,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.HomeworkItem
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintSecondary
import com.example.ui.viewmodel.ScreenDestination
import com.example.ui.viewmodel.VimboxViewModel

@Composable
fun HomeworkScreen(
    viewModel: VimboxViewModel
) {
    BackHandler {
        viewModel.setScreen(ScreenDestination.DASHBOARD)
    }

    val homeworkList by viewModel.homeworkList.collectAsStateWithLifecycle()
    val activeHomework by viewModel.activeSolvingHomework.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredList = when (selectedFilter) {
        "PENDING" -> homeworkList.filter { !it.isCompleted }
        "COMPLETED" -> homeworkList.filter { it.isCompleted }
        else -> homeworkList
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5FD))
            .padding(16.dp)
            .testTag("homework_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MintSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AssignmentTurnedIn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Үй тапсырмалары (Homework)",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp,
                        color = Color(0xFF161A2E)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Мұғалім бекіткен тапсырмаларды орындап, нәтижеңізді және балл өсімін қадағалаңыз.",
                    fontSize = 13.sp,
                    color = Color(0xFF767D9C)
                )
            }
        }

        // Filters row
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterPill(
                    label = "Барлығы (${homeworkList.size})",
                    isSelected = selectedFilter == "ALL",
                    onClick = { selectedFilter = "ALL" }
                )
                FilterPill(
                    label = "Орындалмаған (${homeworkList.count { !it.isCompleted }})",
                    isSelected = selectedFilter == "PENDING",
                    onClick = { selectedFilter = "PENDING" }
                )
                FilterPill(
                    label = "Аяқталған (${homeworkList.count { it.isCompleted }})",
                    isSelected = selectedFilter == "COMPLETED",
                    onClick = { selectedFilter = "COMPLETED" }
                )
            }
        }

        items(filteredList) { hw ->
            HomeworkDetailedCard(
                item = hw,
                onStartSolving = { viewModel.openHomeworkSolver(hw) }
            )
        }
    }

    // Interactive Homework Solver Dialog
    if (activeHomework != null) {
        InteractiveHomeworkModal(
            homework = activeHomework!!,
            onDismiss = { viewModel.closeHomeworkSolver() },
            onFinish = { score ->
                viewModel.completeHomework(activeHomework!!, score)
            }
        )
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) IndigoPrimary else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) IndigoPrimary else Color(0xFFE2E6F5)
        )
    ) {
        Text(
            text = label,
            fontSize = 12.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF374151),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        )
    }
}

@Composable
private fun HomeworkDetailedCard(
    item: HomeworkItem,
    onStartSolving: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF161A2E)
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (item.isCompleted) MintContainer else Color(0xFFFFF0F1)
                ) {
                    Text(
                        text = if (item.isCompleted) "Аяқталды ✓" else "Мерзімі: ${item.deadline}",
                        color = if (item.isCompleted) Color(0xFF065F46) else Color(0xFFD9535E),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${item.subject} · ${item.completedQuestions}/${item.totalQuestions} сұрақ шығарылды",
                fontSize = 12.5.sp,
                color = Color(0xFF767D9C)
            )

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = {
                    if (item.totalQuestions > 0) item.completedQuestions.toFloat() / item.totalQuestions.toFloat() else 0f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (item.isCompleted) MintSecondary else IndigoPrimary,
                trackColor = Color(0xFFE5E9F7)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Балл: ${item.scorePercent}%",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = if (item.scorePercent >= 70) MintSecondary else Color(0xFFF59E0B)
                )

                Button(
                    onClick = onStartSolving,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (item.isCompleted) Color(0xFFF3F5FD) else IndigoPrimary,
                        contentColor = if (item.isCompleted) IndigoPrimary else Color.White
                    ),
                    modifier = Modifier.testTag("solve_hw_${item.id}")
                ) {
                    Icon(
                        imageVector = if (item.isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (item.isCompleted) "Қайта көру" else "Шешуді бастау",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun InteractiveHomeworkModal(
    homework: HomeworkItem,
    onDismiss: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var userAnswers by remember { mutableStateOf(mutableMapOf<Int, Int>()) }
    var isSubmitted by remember { mutableStateOf(false) }

    val sampleQuestions = listOf(
        "1. x^2 - 16 = 0 теңдеуінің оң түбірін табыңыз:" to listOf("A) 2", "B) 4", "C) 8", "D) 16"),
        "2. Үшбұрыштың қабырғалары 3, 4, 5 болса, оның ауданы:" to listOf("A) 6", "B) 10", "C) 12", "D) 15"),
        "3. (2^3) * (2^2) өрнегінің мәні:" to listOf("A) 16", "B) 32", "C) 64", "D) 128")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = homework.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            if (!isSubmitted) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Сұрақ ${currentStep + 1} / ${sampleQuestions.size}:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary
                    )

                    val q = sampleQuestions[currentStep]
                    Text(
                        text = q.first,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.5.sp,
                        color = Color(0xFF161A2E)
                    )

                    q.second.forEachIndexed { optIndex, optText ->
                        val isSelected = userAnswers[currentStep] == optIndex
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    val updated = userAnswers.toMutableMap()
                                    updated[currentStep] = optIndex
                                    userAnswers = updated
                                },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFFEEF0FF) else Color(0xFFF9FAFE),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) IndigoPrimary else Color(0xFFE2E6F5)
                            )
                        ) {
                            Text(
                                text = optText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                fontSize = 13.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = Color(0xFF161A2E)
                            )
                        }
                    }
                }
            } else {
                // Completed Summary
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color(0xFFFFB020),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Тапсырма аяқталды!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF161A2E)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Барлық 3 сұраққа жауап берілді.",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MintContainer
                    ) {
                        Text(
                            text = "Нәтиже: 100% (3/3)",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = Color(0xFF065F46),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (!isSubmitted) {
                if (currentStep < sampleQuestions.size - 1) {
                    Button(
                        onClick = { currentStep++ },
                        enabled = userAnswers[currentStep] != null,
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Text("Келесі →")
                    }
                } else {
                    Button(
                        onClick = { isSubmitted = true },
                        enabled = userAnswers[currentStep] != null,
                        colors = ButtonDefaults.buttonColors(containerColor = MintSecondary)
                    ) {
                        Text("Тапсыру ✓")
                    }
                }
            } else {
                Button(
                    onClick = { onFinish(95) },
                    colors = ButtonDefaults.buttonColors(containerColor = MintSecondary)
                ) {
                    Text("Кабинетке қайту")
                }
            }
        },
        dismissButton = {
            if (!isSubmitted && currentStep > 0) {
                TextButton(onClick = { currentStep-- }) {
                    Text("← Артқа")
                }
            }
        }
    )
}

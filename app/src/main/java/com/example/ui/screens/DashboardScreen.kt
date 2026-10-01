package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.HomeworkItem
import com.example.data.LessonScheduleItem
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintSecondary
import com.example.ui.viewmodel.ScreenDestination
import com.example.ui.viewmodel.VimboxViewModel

@Composable
fun DashboardScreen(
    viewModel: VimboxViewModel
) {
    val isTeacherRole by viewModel.isTeacherRole.collectAsStateWithLifecycle()
    val scheduleItems by viewModel.scheduleList.collectAsStateWithLifecycle()
    val homeworkList by viewModel.homeworkList.collectAsStateWithLifecycle()
    val students by viewModel.studentsList.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5FD))
            .padding(16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top App Bar with Branding and Role Switcher
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(listOf(IndigoPrimary, Color(0xFF8B7BFF)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "V",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Vimbox Tutor",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color(0xFF161A2E)
                        )
                        Text(
                            text = if (isTeacherRole) "Репетитор кабинеті" else "Оқушы кабинеті",
                            fontSize = 12.sp,
                            color = Color(0xFF767D9C)
                        )
                    }
                }

                // Switch Role Pill
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { viewModel.toggleRole() }
                        .testTag("dashboard_toggle_role_btn"),
                    shape = RoundedCornerShape(20.dp),
                    color = if (isTeacherRole) IndigoContainer else MintContainer,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isTeacherRole) Color(0xFFCDD5FA) else Color(0xFFA7F3D0)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isTeacherRole) "👩‍🏫 Репетитор" else "🎓 Оқушы",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (isTeacherRole) IndigoDark else Color(0xFF065F46)
                        )
                    }
                }
            }
        }

        // Hero Banner with Image and Action
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoPrimary),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = if (isTeacherRole) "⚡ Интерактивті сабақ дайын" else "🎯 Келесі сабақ: 14:00",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (isTeacherRole) "Сәлем, Еркем ұстаз! 🚀" else "Сәлем, Айсұлу! 🎓",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isTeacherRole)
                                "Бүгін 3 сабақ жоспарланған. Соңғы ҰБТ жинағынан ${tasks.size} тапсырма дайын тұр."
                            else
                                "Бүгін 14:00-де Планиметрия және Виет тақырыбында жеке сабағыңыз бар.",
                            color = Color.White.copy(alpha = 0.92f),
                            fontSize = 13.5.sp,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.setScreen(ScreenDestination.CLASSROOM) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("start_lesson_hero_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = IndigoPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Vimbox аудиториясына кіру →",
                                color = IndigoPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                }
            }
        }

        // Stats Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = if (isTeacherRole) "Белсенді оқушы" else "Орындалған тест",
                    value = if (isTeacherRole) "${students.size + 14}" else "12",
                    subtext = "+3 осы аптада",
                    isUp = true
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = if (isTeacherRole) "Өткен сабақ" else "Орташа балл",
                    value = if (isTeacherRole) "42" else "78%",
                    subtext = if (isTeacherRole) "+8 айына" else "+9% өсім",
                    isUp = true
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = if (isTeacherRole) "Дайын тест" else "Мақсат балл",
                    value = if (isTeacherRole) "${tasks.size}" else "120",
                    subtext = if (isTeacherRole) "базада" else "ҰБТ 2026",
                    isUp = null
                )
            }
        }

        // Quick Actions Row
        item {
            Text(
                text = "ТЕЗ ӘРЕКЕТТЕР (QUICK ACTIONS):",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B7280),
                letterSpacing = 0.5.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "PDF тест жасау",
                    subtitle = "LaTeX генератор",
                    icon = Icons.Default.AutoAwesome,
                    color = IndigoPrimary,
                    onClick = { viewModel.setScreen(ScreenDestination.GENERATOR) }
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Үй тапсырмасы",
                    subtitle = "${homeworkList.size} белсенді",
                    icon = Icons.Default.Assignment,
                    color = MintSecondary,
                    onClick = { viewModel.setScreen(ScreenDestination.HOMEWORK) }
                )
            }
        }

        // Schedule Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "БҮГІНГІ САБАҚ КЕСТЕСІ:",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B7280),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Барлығын көру →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IndigoPrimary,
                    modifier = Modifier.clickable { viewModel.setScreen(ScreenDestination.STUDENTS) }
                )
            }
        }

        items(scheduleItems) { item ->
            ScheduleRowCard(
                item = item,
                onJoinLesson = { viewModel.setScreen(ScreenDestination.CLASSROOM) }
            )
        }

        // Homework Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "СОҢҒЫ ҮЙ ТАПСЫРМАЛАРЫ:",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B7280),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Тапсырмалар кабинеті →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IndigoPrimary,
                    modifier = Modifier.clickable { viewModel.setScreen(ScreenDestination.HOMEWORK) }
                )
            }
        }

        items(homeworkList.take(2)) { hw ->
            HomeworkRowCard(
                item = hw,
                onClick = {
                    viewModel.openHomeworkSolver(hw)
                    viewModel.setScreen(ScreenDestination.HOMEWORK)
                }
            )
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    subtext: String,
    isUp: Boolean?
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF767D9C)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF161A2E)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtext,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isUp == true) MintSecondary else Color(0xFF6B7280)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF161A2E)
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF767D9C)
                )
            }
        }
    }
}

@Composable
private fun ScheduleRowCard(
    item: LessonScheduleItem,
    onJoinLesson: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onJoinLesson),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEEF0FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.time,
                        color = IndigoPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = item.studentName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF161A2E)
                    )
                    Text(
                        text = "${item.subject} · ${item.topic}",
                        fontSize = 12.sp,
                        color = Color(0xFF767D9C)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (item.track) {
                    "ҰБТ" -> Color(0xFFEEF0FF)
                    "НИШ" -> Color(0xFFE8FAF4)
                    else -> Color(0xFFFFF0F1)
                }
            ) {
                Text(
                    text = item.track,
                    color = when (item.track) {
                        "ҰБТ" -> IndigoDark
                        "НИШ" -> Color(0xFF0E9E7B)
                        else -> Color(0xFFD9535E)
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun HomeworkRowCard(
    item: HomeworkItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF161A2E)
                )
                Text(
                    text = if (item.isCompleted) "Орындалды ✓" else item.deadline,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (item.isCompleted) MintSecondary else Color(0xFFFF7A85)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${item.subject} · ${item.completedQuestions}/${item.totalQuestions} сұрақ",
                fontSize = 12.sp,
                color = Color(0xFF767D9C)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = {
                    if (item.totalQuestions > 0) item.completedQuestions.toFloat() / item.totalQuestions.toFloat() else 0f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (item.isCompleted) MintSecondary else IndigoPrimary,
                trackColor = Color(0xFFE5E9F7)
            )
        }
    }
}

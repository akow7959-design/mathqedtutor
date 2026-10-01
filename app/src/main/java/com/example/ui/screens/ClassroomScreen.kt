package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ChatMessage
import com.example.data.LessonTask
import com.example.ui.components.LessonPlanTimeline
import com.example.ui.components.TeacherCribCard
import com.example.ui.components.VideoCallBar
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.CoralTertiary
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintContainer
import com.example.ui.theme.MintSecondary
import com.example.ui.viewmodel.ScreenDestination
import com.example.ui.viewmodel.VimboxViewModel
import com.example.ui.whiteboard.WhiteboardCanvas
import com.example.ui.whiteboard.WhiteboardToolbar
import kotlinx.coroutines.delay

@Composable
fun ClassroomScreen(
    viewModel: VimboxViewModel
) {
    BackHandler {
        viewModel.setScreen(ScreenDestination.DASHBOARD)
    }

    val isTeacherRole by viewModel.isTeacherRole.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val currentTaskIndex by viewModel.currentTaskIndex.collectAsStateWithLifecycle()
    val selectedOptionIndex by viewModel.selectedOptionIndex.collectAsStateWithLifecycle()
    val isAnswerChecked by viewModel.isAnswerChecked.collectAsStateWithLifecycle()
    val showHint1 by viewModel.showHint1.collectAsStateWithLifecycle()
    val showHint2 by viewModel.showHint2.collectAsStateWithLifecycle()
    val classroomTab by viewModel.classroomTab.collectAsStateWithLifecycle()
    val activeStageId by viewModel.activeStageId.collectAsStateWithLifecycle()

    val boardElements by viewModel.boardElements.collectAsStateWithLifecycle()
    val currentTool by viewModel.currentTool.collectAsStateWithLifecycle()
    val currentColor by viewModel.currentColor.collectAsStateWithLifecycle()
    val isAutoFixEnabled by viewModel.isAutoFixEnabled.collectAsStateWithLifecycle()
    val showGrid by viewModel.showGrid.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()

    // Live ticking timer (45:00 countdown)
    var elapsedSeconds by remember { mutableIntStateOf(1280) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            elapsedSeconds++
        }
    }
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val formattedTimer = String.format("%02d:%02d", minutes, seconds)

    val currentTask = tasks.getOrNull(currentTaskIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5FD))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("classroom_screen")
    ) {
        // Video Calling Tile
        VideoCallBar(
            isTeacherRole = isTeacherRole,
            onToggleRole = { viewModel.toggleRole() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Task Header with Progress & Navigation
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IndigoContainer
                        ) {
                            Text(
                                text = "Тапсырма ${currentTaskIndex + 1}/${tasks.size}",
                                color = IndigoPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentTask?.examTrack ?: "ҰБТ",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6B7280)
                        )
                        Text(
                            text = " · ${currentTask?.subject ?: "Математика"}",
                            fontSize = 11.5.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }

                    // Timer & Next / Prev controls
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF9FAFD))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = Color(0xFF4B5563),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formattedTimer,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F2937)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = { viewModel.prevTask() },
                            enabled = currentTaskIndex > 0,
                            modifier = Modifier.size(32.dp).testTag("prev_task_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Prev",
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.nextTask() },
                            enabled = currentTaskIndex < tasks.size - 1,
                            modifier = Modifier.size(32.dp).testTag("next_task_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress line
                LinearProgressIndicator(
                    progress = {
                        if (tasks.isNotEmpty()) (currentTaskIndex + 1).toFloat() / tasks.size.toFloat() else 0f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = IndigoPrimary,
                    trackColor = Color(0xFFE5E9F7),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Classroom Tabs: Whiteboard, Task & Solve, Teacher Crib, In-Class Chat
        val tabs = listOf(
            "✏️ Интерактивті тақта",
            "📋 Тапсырма мен шешу",
            if (isTeacherRole) "🔒 Мұғалім сүйемелі" else "💡 Шпаргалка",
            "💬 Чат (${chatMessages.size})"
        )

        ScrollableTabRow(
            selectedTabIndex = classroomTab,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = IndigoPrimary,
            indicator = { tabPositions ->
                if (classroomTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[classroomTab]),
                        color = IndigoPrimary,
                        height = 3.dp
                    )
                }
            },
            divider = { HorizontalDivider(color = Color(0xFFE2E6F5)) }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = classroomTab == index,
                    onClick = { viewModel.setClassroomTab(index) },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (classroomTab == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (classroomTab == index) IndigoPrimary else Color(0xFF6B7280)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (classroomTab) {
                0 -> {
                    // Whiteboard Tab
                    Column(modifier = Modifier.fillMaxSize()) {
                        WhiteboardToolbar(
                            currentTool = currentTool,
                            onToolSelected = { viewModel.setTool(it) },
                            currentColor = currentColor,
                            onColorSelected = { viewModel.setColor(it) },
                            isAutoFixEnabled = isAutoFixEnabled,
                            onToggleAutoFix = { viewModel.toggleAutoFix(it) },
                            showGrid = showGrid,
                            onToggleGrid = { viewModel.toggleGrid(it) },
                            onUndo = { viewModel.undoBoard() },
                            onClear = { viewModel.clearBoard() },
                            onAddFormula = { viewModel.addFormulaSticker(it) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        WhiteboardCanvas(
                            modifier = Modifier.weight(1f),
                            elements = boardElements,
                            onElementAdded = { viewModel.addBoardElement(it) },
                            onElementDeleted = { viewModel.deleteBoardElement(it) },
                            onFormulaMoved = { id, offset -> viewModel.moveFormula(id, offset) },
                            currentTool = currentTool,
                            currentColor = currentColor,
                            strokeWidth = 4f,
                            isAutoFixEnabled = isAutoFixEnabled,
                            showGrid = showGrid
                        )
                    }
                }
                1 -> {
                    // Task & Practice Tab
                    if (currentTask != null) {
                        TaskSolveView(
                            task = currentTask,
                            selectedIndex = selectedOptionIndex,
                            isAnswerChecked = isAnswerChecked,
                            showHint1 = showHint1,
                            showHint2 = showHint2,
                            onSelectOption = { viewModel.selectOption(it) },
                            onCheckAnswer = { viewModel.checkAnswer() },
                            onToggleHint1 = { viewModel.toggleHint1() },
                            onToggleHint2 = { viewModel.toggleHint2() },
                            onNext = { viewModel.nextTask() }
                        )
                    }
                }
                2 -> {
                    // Teacher's Crib Tab
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            TeacherCribCard()
                        }
                    }
                }
                3 -> {
                    // In-Class Chat Tab
                    InClassChatView(
                        messages = chatMessages,
                        onSendMessage = { viewModel.sendMessage(it) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lesson Plan timeline drawer at the bottom
        LessonPlanTimeline(
            activeStageId = activeStageId,
            onStageSelected = { viewModel.setActiveStage(it) }
        )
    }
}

@Composable
private fun TaskSolveView(
    task: LessonTask,
    selectedIndex: Int?,
    isAnswerChecked: Boolean,
    showHint1: Boolean,
    showHint2: Boolean,
    onSelectOption: (Int) -> Unit,
    onCheckAnswer: () -> Unit,
    onToggleHint1: () -> Unit,
    onToggleHint2: () -> Unit,
    onNext: () -> Unit
) {
    val options = task.getOptionsList()
    val isCorrect = selectedIndex == task.correctIndex

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Question Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(IndigoPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${task.orderNo}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Есеп шарты:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF1E243A)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = task.question,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF161A2E),
                        lineHeight = 24.sp
                    )
                }
            }
        }

        // Options List
        item {
            Text(
                text = "ЖАУАП НҰСҚАСЫН ТАҢДАҢЫЗ:",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B7280),
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }

        items(options.indices.toList()) { index ->
            val isSelected = selectedIndex == index
            val isThisCorrect = index == task.correctIndex

            val backgroundColor = when {
                isAnswerChecked && isThisCorrect -> MintContainer
                isAnswerChecked && isSelected && !isThisCorrect -> CoralContainer
                isSelected -> Color(0xFFEEF0FF)
                else -> Color.White
            }

            val borderColor = when {
                isAnswerChecked && isThisCorrect -> MintSecondary
                isAnswerChecked && isSelected && !isThisCorrect -> CoralTertiary
                isSelected -> IndigoPrimary
                else -> Color(0xFFE5E9F7)
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelectOption(index) }
                    .testTag("option_$index"),
                shape = RoundedCornerShape(14.dp),
                color = backgroundColor,
                border = androidx.compose.foundation.BorderStroke(if (isSelected || (isAnswerChecked && isThisCorrect)) 2.dp else 1.dp, borderColor)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isAnswerChecked && isThisCorrect -> MintSecondary
                                    isAnswerChecked && isSelected && !isThisCorrect -> CoralTertiary
                                    isSelected -> IndigoPrimary
                                    else -> Color(0xFFF3F5FD)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${('A' + index)}",
                            color = if (isSelected || (isAnswerChecked && isThisCorrect)) Color.White else Color(0xFF4B5563),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = options[index],
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = Color(0xFF161A2E)
                    )
                }
            }
        }

        // Action Buttons: Check Answer & Next
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onCheckAnswer,
                    enabled = selectedIndex != null && !isAnswerChecked,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("check_answer_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("Жауапты тексеру ✓", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                if (isAnswerChecked) {
                    Button(
                        onClick = onNext,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("next_task_action_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MintSecondary)
                    ) {
                        Text("Келесі тапсырма →", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        // Animated Answer Feedback & Explanation Card
        if (isAnswerChecked) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isCorrect) MintContainer else CoralContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isCorrect) MintSecondary else CoralTertiary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (isCorrect) MintSecondary else CoralTertiary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isCorrect) "Дұрыс шешім! Жинаған балл: +10" else "Қате жауап, қайта ойланыңыз!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isCorrect) Color(0xFF065F46) else Color(0xFF991B1B)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = task.explanation,
                            fontSize = 13.5.sp,
                            color = Color(0xFF1E243A),
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Формула: ${task.formula}",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = IndigoPrimary
                        )
                    }
                }
            }
        }

        // Hints Accordion
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleHint1() },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = Color(0xFFFFB020),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "1-деңгейлі көмек (Бағыттау)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E243A)
                            )
                        }
                        if (showHint1) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = task.hintStep1.ifEmpty { "Берілген есептің негізгі коэффициенттерін анықтап алыңыз." },
                                fontSize = 13.sp,
                                color = Color(0xFF4B5563)
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleHint2() },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = IndigoPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "2-деңгейлі көмек (Формула)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E243A)
                            )
                        }
                        if (showHint2) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = task.hintStep2.ifEmpty { "Виет теоремасын немесе дискриминантты қолданыңыз." },
                                fontSize = 13.sp,
                                color = Color(0xFF4B5563)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InClassChatView(
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    val mathSymbols = listOf("√", "x²", "±", "π", "÷", "≠", "≤", "≥", "·")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE5E9F7), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        // Message List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                val isTutor = msg.isFromTutor
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isTutor) Alignment.End else Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = msg.senderName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = if (isTutor) IndigoPrimary else MintSecondary
                        )
                        Text(
                            text = msg.timestamp,
                            fontSize = 10.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = if (isTutor) 12.dp else 2.dp,
                            bottomEnd = if (isTutor) 2.dp else 12.dp
                        ),
                        color = if (isTutor) Color(0xFFEEF0FF) else Color(0xFFF3F4F6),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isTutor) Color(0xFFD4DAFB) else Color(0xFFE5E7EB)
                        )
                    ) {
                        Text(
                            text = msg.text,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            fontSize = 13.5.sp,
                            color = Color(0xFF1F2937)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Math symbol keyboard row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            mathSymbols.forEach { sym ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { textInput += sym },
                    color = Color(0xFFF3F5FD),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD8DEFB))
                ) {
                    Text(
                        text = sym,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Input row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Хабарлама немесе сұрақ...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            Button(
                onClick = {
                    if (textInput.isNotBlank()) {
                        onSendMessage(textInput)
                        textInput = ""
                    }
                },
                enabled = textInput.isNotBlank(),
                modifier = Modifier
                    .height(50.dp)
                    .testTag("send_chat_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}

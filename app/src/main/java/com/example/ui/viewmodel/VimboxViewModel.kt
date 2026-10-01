package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.ChatMessage
import com.example.data.HomeworkItem
import com.example.data.LessonScheduleItem
import com.example.data.LessonTask
import com.example.data.StudentProfile
import com.example.data.VimboxDatabase
import com.example.ui.whiteboard.BoardElement
import com.example.ui.whiteboard.WhiteboardTool
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenDestination {
    DASHBOARD,
    CLASSROOM,
    GENERATOR,
    HOMEWORK,
    STUDENTS
}

class VimboxViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository

    init {
        val database = VimboxDatabase.getDatabase(application)
        repository = AppRepository(database.appDao())
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Role & Navigation
    private val _isTeacherRole = MutableStateFlow(true)
    val isTeacherRole: StateFlow<Boolean> = _isTeacherRole.asStateFlow()

    private val _currentScreen = MutableStateFlow(ScreenDestination.DASHBOARD)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // Tasks & Classroom State
    val tasks: StateFlow<List<LessonTask>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentTaskIndex = MutableStateFlow(0)
    val currentTaskIndex: StateFlow<Int> = _currentTaskIndex.asStateFlow()

    private val _selectedOptionIndex = MutableStateFlow<Int?>(null)
    val selectedOptionIndex: StateFlow<Int?> = _selectedOptionIndex.asStateFlow()

    private val _isAnswerChecked = MutableStateFlow(false)
    val isAnswerChecked: StateFlow<Boolean> = _isAnswerChecked.asStateFlow()

    private val _showHint1 = MutableStateFlow(false)
    val showHint1: StateFlow<Boolean> = _showHint1.asStateFlow()

    private val _showHint2 = MutableStateFlow(false)
    val showHint2: StateFlow<Boolean> = _showHint2.asStateFlow()

    private val _activeStageId = MutableStateFlow(2)
    val activeStageId: StateFlow<Int> = _activeStageId.asStateFlow()

    private val _classroomTab = MutableStateFlow(0) // 0: Whiteboard, 1: Task, 2: Crib, 3: Chat
    val classroomTab: StateFlow<Int> = _classroomTab.asStateFlow()

    // Whiteboard State
    private val _boardElements = MutableStateFlow<List<BoardElement>>(emptyList())
    val boardElements: StateFlow<List<BoardElement>> = _boardElements.asStateFlow()

    private val _currentTool = MutableStateFlow(WhiteboardTool.PEN)
    val currentTool: StateFlow<WhiteboardTool> = _currentTool.asStateFlow()

    private val _currentColor = MutableStateFlow(Color(0xFF161A2E))
    val currentColor: StateFlow<Color> = _currentColor.asStateFlow()

    private val _isAutoFixEnabled = MutableStateFlow(true)
    val isAutoFixEnabled: StateFlow<Boolean> = _isAutoFixEnabled.asStateFlow()

    private val _showGrid = MutableStateFlow(false)
    val showGrid: StateFlow<Boolean> = _showGrid.asStateFlow()

    // Chat
    val chatMessages: StateFlow<List<ChatMessage>> = repository.allChatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Homework
    val homeworkList: StateFlow<List<HomeworkItem>> = repository.allHomework
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeSolvingHomework = MutableStateFlow<HomeworkItem?>(null)
    val activeSolvingHomework: StateFlow<HomeworkItem?> = _activeSolvingHomework.asStateFlow()

    // Students & Schedule
    val studentsList: StateFlow<List<StudentProfile>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scheduleList: StateFlow<List<LessonScheduleItem>> = repository.allSchedule
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Generator State
    private val _generatorFormat = MutableStateFlow("ENT")
    val generatorFormat: StateFlow<String> = _generatorFormat.asStateFlow()

    private val _parsedTasks = MutableStateFlow<List<LessonTask>>(emptyList())
    val parsedTasks: StateFlow<List<LessonTask>> = _parsedTasks.asStateFlow()

    private val _isParsing = MutableStateFlow(false)
    val isParsing: StateFlow<Boolean> = _isParsing.asStateFlow()

    // Actions
    fun toggleRole() {
        _isTeacherRole.value = !_isTeacherRole.value
    }

    fun setScreen(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    fun setClassroomTab(tab: Int) {
        _classroomTab.value = tab
    }

    fun selectTaskIndex(index: Int) {
        val total = tasks.value.size
        if (index in 0 until total) {
            _currentTaskIndex.value = index
            _selectedOptionIndex.value = null
            _isAnswerChecked.value = false
            _showHint1.value = false
            _showHint2.value = false
        }
    }

    fun nextTask() {
        val current = _currentTaskIndex.value
        if (current + 1 < tasks.value.size) {
            selectTaskIndex(current + 1)
        }
    }

    fun prevTask() {
        val current = _currentTaskIndex.value
        if (current > 0) {
            selectTaskIndex(current - 1)
        }
    }

    fun selectOption(index: Int) {
        _selectedOptionIndex.value = index
        _isAnswerChecked.value = false
    }

    fun checkAnswer() {
        _isAnswerChecked.value = true
    }

    fun toggleHint1() {
        _showHint1.value = !_showHint1.value
    }

    fun toggleHint2() {
        _showHint2.value = !_showHint2.value
    }

    fun setActiveStage(stageId: Int) {
        _activeStageId.value = stageId
    }

    // Whiteboard actions
    fun setTool(tool: WhiteboardTool) {
        _currentTool.value = tool
    }

    fun setColor(color: Color) {
        _currentColor.value = color
    }

    fun toggleAutoFix(enabled: Boolean) {
        _isAutoFixEnabled.value = enabled
    }

    fun toggleGrid(show: Boolean) {
        _showGrid.value = show
    }

    fun addBoardElement(element: BoardElement) {
        _boardElements.value = _boardElements.value + element
    }

    fun deleteBoardElement(element: BoardElement) {
        _boardElements.value = _boardElements.value - element
    }

    fun undoBoard() {
        if (_boardElements.value.isNotEmpty()) {
            _boardElements.value = _boardElements.value.dropLast(1)
        }
    }

    fun clearBoard() {
        _boardElements.value = emptyList()
    }

    fun moveFormula(id: String, newPos: Offset) {
        _boardElements.value = _boardElements.value.map {
            if (it is BoardElement.Formula && it.id == id) {
                it.copy(position = newPos)
            } else it
        }
    }

    fun addFormulaSticker(formulaText: String) {
        val formula = BoardElement.Formula(
            id = "f_${System.currentTimeMillis()}",
            text = formulaText,
            position = Offset(80f, 100f),
            color = _currentColor.value
        )
        addBoardElement(formula)
    }

    // Chat actions
    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val isTeacher = _isTeacherRole.value
        viewModelScope.launch {
            val message = ChatMessage(
                senderName = if (isTeacher) "Мұғалім" else "Айсұлу",
                text = text,
                timestamp = "Бүгін, ${java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}",
                isFromTutor = isTeacher
            )
            repository.sendChatMessage(message)
        }
    }

    // Homework actions
    fun openHomeworkSolver(item: HomeworkItem) {
        _activeSolvingHomework.value = item
    }

    fun closeHomeworkSolver() {
        _activeSolvingHomework.value = null
    }

    fun completeHomework(item: HomeworkItem, newScore: Int) {
        viewModelScope.launch {
            repository.updateHomework(
                item.copy(
                    completedQuestions = item.totalQuestions,
                    scorePercent = newScore,
                    isCompleted = true
                )
            )
            _activeSolvingHomework.value = null
        }
    }

    // Add Student
    fun addNewStudent(name: String, track: String, grade: Int, targetScore: Int) {
        viewModelScope.launch {
            repository.insertStudent(
                StudentProfile(
                    name = name,
                    track = track,
                    grade = grade,
                    averageScore = 70,
                    targetScore = targetScore,
                    lastLessonDate = "Жаңа",
                    avatarColorHex = 0xFF5B6CF9
                )
            )
        }
    }

    // Generator actions
    fun setGeneratorFormat(format: String) {
        _generatorFormat.value = format
    }

    fun parseInputText(rawText: String) {
        viewModelScope.launch {
            _isParsing.value = true
            delay(500) // smooth parsing animation feel

            val lines = rawText.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
            val generated = mutableListOf<LessonTask>()
            var curNo = 1
            var curQuestion = ""
            var curOptions = mutableListOf<String>()

            for (line in lines) {
                if (line.matches(Regex("^(\\d+)[.)].*"))) {
                    if (curQuestion.isNotEmpty()) {
                        generated.add(
                            createTaskFromParsed(
                                curNo,
                                curQuestion,
                                curOptions,
                                _generatorFormat.value
                            )
                        )
                        curNo++
                        curOptions = mutableListOf()
                    }
                    curQuestion = line.replaceFirst(Regex("^(\\d+)[.)]\\s*"), "")
                } else if (line.matches(Regex("^[A-EА-Е][.)].*"))) {
                    curOptions.add(line)
                } else {
                    curQuestion += " $line"
                }
            }
            if (curQuestion.isNotEmpty()) {
                generated.add(
                    createTaskFromParsed(
                        curNo,
                        curQuestion,
                        curOptions,
                        _generatorFormat.value
                    )
                )
            }

            if (generated.isEmpty()) {
                // If single line
                generated.add(
                    createTaskFromParsed(
                        1,
                        rawText,
                        listOf("A) Дұрыс", "B) Қате"),
                        _generatorFormat.value
                    )
                )
            }

            _parsedTasks.value = generated
            _isParsing.value = false
        }
    }

    private fun createTaskFromParsed(
        no: Int,
        question: String,
        options: List<String>,
        format: String
    ): LessonTask {
        val formattedQuestion = formatMathSymbols(question)
        val opts = if (options.isNotEmpty()) {
            options.joinToString(" | ")
        } else {
            "A) 12 | B) 16 | C) 24 | D) 30 | E) 48"
        }

        return LessonTask(
            orderNo = no,
            question = formattedQuestion,
            options = opts,
            correctIndex = 0,
            explanation = "Тапсырманың теориялық шешімі мен заңдылығы.",
            formula = "f(x) = y",
            subject = "Математика",
            examTrack = format,
            hintStep1 = "Теңдеудегі шартты жүйеге келтіріңіз",
            hintStep2 = "Нұсқаларды тексеру әдісін қолданыңыз"
        )
    }

    private fun formatMathSymbols(text: String): String {
        return text
            .replace("sqrt", "√")
            .replace("^2", "²")
            .replace("^3", "³")
            .replace("<=", "≤")
            .replace(">=", "≥")
            .replace("!=", "≠")
            .replace("*", "·")
    }

    fun saveParsedTasksToClassroom() {
        viewModelScope.launch {
            if (_parsedTasks.value.isNotEmpty()) {
                repository.insertTasks(_parsedTasks.value)
                _parsedTasks.value = emptyList()
                _currentScreen.value = ScreenDestination.CLASSROOM
            }
        }
    }
}

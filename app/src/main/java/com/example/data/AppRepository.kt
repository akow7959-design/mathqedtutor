package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val dao: AppDao) {
    val allTasks: Flow<List<LessonTask>> = dao.getAllTasks()
    val allHomework: Flow<List<HomeworkItem>> = dao.getAllHomework()
    val allStudents: Flow<List<StudentProfile>> = dao.getAllStudents()
    val allChatMessages: Flow<List<ChatMessage>> = dao.getAllChatMessages()
    val allSchedule: Flow<List<LessonScheduleItem>> = dao.getAllScheduleItems()

    suspend fun insertTask(task: LessonTask) = dao.insertTask(task)
    suspend fun insertTasks(tasks: List<LessonTask>) = dao.insertTasks(tasks)
    suspend fun deleteTask(id: Int) = dao.deleteTask(id)

    suspend fun insertHomework(item: HomeworkItem) = dao.insertHomework(item)
    suspend fun updateHomework(item: HomeworkItem) = dao.updateHomework(item)

    suspend fun insertStudent(student: StudentProfile) = dao.insertStudent(student)
    suspend fun sendChatMessage(message: ChatMessage) = dao.insertChatMessage(message)

    suspend fun seedInitialDataIfEmpty() {
        val initialTasks = listOf(
            LessonTask(
                orderNo = 1,
                question = "x² - 5x + 6 = 0 теңдеуінің түбірлерінің қосындысын табыңыз.",
                options = "A) 2 | B) 3 | C) 5 | D) 6 | E) -5",
                correctIndex = 2,
                explanation = "Виет теоремасы бойынша: келтірілген x² + px + q = 0 теңдеуі үшін x₁ + x₂ = -p. Мұнда p = -5, ендеше қосындысы -(-5) = 5.",
                formula = "x₁ + x₂ = -p,  x₁ · x₂ = q",
                subject = "Алгебра",
                examTrack = "ҰБТ",
                hintStep1 = "Теңдеудегі коэффициенттер: a=1, b=-5, c=6",
                hintStep2 = "Виет теоремасын еске түсіріңіз: түбірлер қосындысы -b/a-ға тең"
            ),
            LessonTask(
                orderNo = 2,
                question = "√(49) + 3² өрнегінің мәнін есептеңіз.",
                options = "A) 14 | B) 16 | C) 18 | D) 20 | E) 12",
                correctIndex = 1,
                explanation = "√49 = 7 және 3² = 9. Олардың қосындысы: 7 + 9 = 16.",
                formula = "√a² = |a|, aⁿ",
                subject = "Арифметика",
                examTrack = "ҰБТ",
                hintStep1 = "Алдымен түбір мен дәрежені жеке-жеке есептеңіз",
                hintStep2 = "7 + 9 өрнегін қосыңыз"
            ),
            LessonTask(
                orderNo = 3,
                question = "y = 3x² - 4x + 1 функциясының туындысын табыңыз.",
                options = "A) 6x - 4 | B) 3x - 4 | C) 6x² - 4 | D) 6x | E) 3x²",
                correctIndex = 0,
                explanation = "(xⁿ)' = n·xⁿ⁻¹ және (kx)' = k, c' = 0 формуласы бойынша: (3x²)' = 6x, (-4x)' = -4, (1)' = 0. Қорытынды: y' = 6x - 4.",
                formula = "(xⁿ)' = n · xⁿ⁻¹,  (c)' = 0",
                subject = "Математикалық талдау",
                examTrack = "ҰБТ",
                hintStep1 = "Дәрежелік функцияның туындысының формуласын қолданыңыз",
                hintStep2 = "3 · 2x¹ - 4 = 6x - 4"
            ),
            LessonTask(
                orderNo = 4,
                question = "2, 6, 18, 54, ... сандық қатарының келесі 5-ші мүшесін табыңыз.",
                options = "A) 108 | B) 162 | C) 216 | D) 148",
                correctIndex = 1,
                explanation = "Бұл еселігі q = 3 болатын геометриялық прогрессия: 2·3=6, 6·3=18, 18·3=54, 54·3 = 162.",
                formula = "bₙ = b₁ · qⁿ⁻¹",
                subject = "Сандық логика",
                examTrack = "НИШ",
                hintStep1 = "Көршілес сандар арасындағы қатынасты табыңыз: 6/2 = 3, 18/6 = 3",
                hintStep2 = "54-ті 3-ке көбейтіңіз"
            ),
            LessonTask(
                orderNo = 5,
                question = "Тікбұрышты үшбұрыштың катеттері a = 6, b = 8 болса, гипотенузасын табыңыз.",
                options = "A) 10 | B) 12 | C) 14 | D) √52",
                correctIndex = 0,
                explanation = "Пифагор теоремасы бойынша c² = a² + b² = 6² + 8² = 36 + 64 = 100. c = √100 = 10.",
                formula = "c² = a² + b²,  c = √(a² + b²)",
                subject = "Геометрия",
                examTrack = "БИЛ",
                hintStep1 = "Пифагор теоремасын пайдаланыңыз",
                hintStep2 = "36 + 64 = 100, √100 = ?"
            )
        )
        dao.insertTasks(initialTasks)

        val initialHomework = listOf(
            HomeworkItem(
                title = "Планиметрия және үшбұрыштар",
                subject = "Геометрия (ҰБТ)",
                totalQuestions = 16,
                completedQuestions = 12,
                scorePercent = 75,
                deadline = "Бүгін, 20:00",
                isCompleted = false
            ),
            HomeworkItem(
                title = "Туынды және экстремумдар",
                subject = "Математика (ҰБТ)",
                totalQuestions = 10,
                completedQuestions = 3,
                scorePercent = 30,
                deadline = "Ертең, 18:00",
                isCompleted = false
            ),
            HomeworkItem(
                title = "Сандық қатарлар мен заңдылықтар",
                subject = "Логика (НИШ)",
                totalQuestions = 15,
                completedQuestions = 15,
                scorePercent = 93,
                deadline = "Кеше",
                isCompleted = true
            )
        )
        dao.insertAllHomework(initialHomework)

        val initialStudents = listOf(
            StudentProfile(
                name = "Айсұлу Қуаныш",
                track = "ҰБТ",
                grade = 11,
                averageScore = 78,
                targetScore = 120,
                lastLessonDate = "Бүгін, 14:00",
                avatarColorHex = 0xFF5B6CF9
            ),
            StudentProfile(
                name = "Диас Серік",
                track = "НИШ",
                grade = 6,
                averageScore = 64,
                targetScore = 95,
                lastLessonDate = "Кеше, 16:30",
                avatarColorHex = 0xFF19C39C
            ),
            StudentProfile(
                name = "Мадина Оспан",
                track = "БИЛ",
                grade = 6,
                averageScore = 85,
                targetScore = 100,
                lastLessonDate = "3 күн бұрын",
                avatarColorHex = 0xFFFF7A85
            ),
            StudentProfile(
                name = "Ернар Әлиев",
                track = "РФМШ",
                grade = 9,
                averageScore = 82,
                targetScore = 110,
                lastLessonDate = "Өткен аптада",
                avatarColorHex = 0xFFFFB020
            )
        )
        dao.insertAllStudents(initialStudents)

        val initialChat = listOf(
            ChatMessage(
                senderName = "Айсұлу",
                text = "Сәлеметсіз бе! Мен сабаққа қосылдым 👋",
                timestamp = "14:02",
                isFromTutor = false
            ),
            ChatMessage(
                senderName = "Мұғалім",
                text = "Сәлем, Айсұлу! Бүгінгі тақырып — квадрат теңдеулер мен Виет теоремасы. Тақтаға қараңыз ✏️",
                timestamp = "14:03",
                isFromTutor = true
            ),
            ChatMessage(
                senderName = "Айсұлу",
                text = "Түсіндім, бірінші есептің дискриминанты оң мән береді ме?",
                timestamp = "14:04",
                isFromTutor = false
            ),
            ChatMessage(
                senderName = "Мұғалім",
                text = "Иә, D = (-5)² - 4·1·6 = 25 - 24 = 1. Демек екі нақты түбір бар!",
                timestamp = "14:05",
                isFromTutor = true
            )
        )
        dao.insertAllChatMessages(initialChat)

        val initialSchedule = listOf(
            LessonScheduleItem(
                time = "14:00",
                studentName = "Айсұлу Қуаныш",
                subject = "Алгебра",
                topic = "Планиметрия және Виет",
                track = "ҰБТ"
            ),
            LessonScheduleItem(
                time = "16:00",
                studentName = "Диас Серік",
                subject = "Математикалық сауаттылық",
                topic = "Сандық қатарлар мен заңдылық",
                track = "НИШ"
            ),
            LessonScheduleItem(
                time = "18:30",
                studentName = "6-сынып тобы (Мадина, Ернар)",
                subject = "Логика",
                topic = "Дирихле принципі және графтар",
                track = "БИЛ"
            )
        )
        dao.insertAllSchedule(initialSchedule)
    }
}

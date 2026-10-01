package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_tasks")
data class LessonTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderNo: Int,
    val question: String,
    val options: String, // Pipe-separated options, e.g. "A) 2 | B) 3 | C) 5 | D) 6 | E) -5"
    val correctIndex: Int,
    val explanation: String,
    val formula: String,
    val subject: String,
    val examTrack: String, // ENT, NIS, BIL, SCHOOL
    val hintStep1: String = "",
    val hintStep2: String = ""
) {
    fun getOptionsList(): List<String> = options.split(" | ").map { it.trim() }
}

@Entity(tableName = "homework_items")
data class HomeworkItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subject: String,
    val totalQuestions: Int,
    val completedQuestions: Int,
    val scorePercent: Int,
    val deadline: String,
    val isCompleted: Boolean
)

@Entity(tableName = "student_profiles")
data class StudentProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val track: String, // ҰБТ, НИШ, БИЛ
    val grade: Int,
    val averageScore: Int,
    val targetScore: Int,
    val lastLessonDate: String,
    val avatarColorHex: Long
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isFromTutor: Boolean
)

@Entity(tableName = "schedule_items")
data class LessonScheduleItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: String,
    val studentName: String,
    val subject: String,
    val topic: String,
    val track: String
)

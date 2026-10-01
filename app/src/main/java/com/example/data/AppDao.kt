package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Lesson Tasks
    @Query("SELECT * FROM lesson_tasks ORDER BY orderNo ASC")
    fun getAllTasks(): Flow<List<LessonTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: LessonTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<LessonTask>)

    @Query("DELETE FROM lesson_tasks WHERE id = :id")
    suspend fun deleteTask(id: Int)

    // Homework
    @Query("SELECT * FROM homework_items ORDER BY isCompleted ASC, id DESC")
    fun getAllHomework(): Flow<List<HomeworkItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomework(item: HomeworkItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHomework(items: List<HomeworkItem>)

    @Update
    suspend fun updateHomework(item: HomeworkItem)

    // Students
    @Query("SELECT * FROM student_profiles ORDER BY id DESC")
    fun getAllStudents(): Flow<List<StudentProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentProfile): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStudents(students: List<StudentProfile>)

    // Chat
    @Query("SELECT * FROM chat_messages ORDER BY id ASC")
    fun getAllChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllChatMessages(messages: List<ChatMessage>)

    // Schedule
    @Query("SELECT * FROM schedule_items ORDER BY id ASC")
    fun getAllScheduleItems(): Flow<List<LessonScheduleItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSchedule(items: List<LessonScheduleItem>)
}

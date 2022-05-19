package com.example.studentlife

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.*
@Entity
data class Quiz(@PrimaryKey val id: UUID = UUID.randomUUID(),val subject: String, val mode: String = "",var score: Double =0.0,val date: Date = Date())



data class QuizQuestions(@Embedded val quiz: Quiz,
                         @Relation(
    parentColumn = "id",
    entityColumn = "quiz_id")
                         val questions: List<Question>

)
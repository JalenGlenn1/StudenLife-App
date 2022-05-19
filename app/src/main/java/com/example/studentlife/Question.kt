package com.example.studentlife

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Question(@PrimaryKey val id: UUID = UUID.randomUUID(),
                    val prompt: String = "",
                    var answer: String = "",
                    var correct_answer: String,
                    val quiz_id: UUID,
)
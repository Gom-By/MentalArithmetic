package com.example.mentalarithmetic.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mentalarithmetic.domain.Difficulty
import java.util.UUID

@Entity
class PlayerEntity (
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String = "No Name",
    val score: Int = 0,
    val lives: Int = 0,
    val gameMode: Difficulty = Difficulty.EASY
)
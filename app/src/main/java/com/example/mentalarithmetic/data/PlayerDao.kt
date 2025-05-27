package com.example.mentalarithmetic.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PlayerDao {
    @Query("SELECT * FROM PlayerEntity")
    abstract fun getPlayers(): Flow<List<PlayerEntity>>

    @Insert
    abstract fun insertPlayer(player: PlayerEntity)

    @Delete
    abstract fun deletePlayer(player: PlayerEntity)

    @Delete
    abstract fun deletePlayers(entities: List<PlayerEntity>)

    @Query("SELECT * FROM PlayerEntity ORDER BY score DESC LIMIT 5")
    abstract fun getTopPlayers(): Flow<List<PlayerEntity>>
}
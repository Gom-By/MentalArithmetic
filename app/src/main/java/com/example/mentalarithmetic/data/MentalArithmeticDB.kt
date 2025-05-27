package com.example.mentalarithmetic.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PlayerEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class MentalArithmeticDB: RoomDatabase() {
    abstract fun MentalArithmeticDao(): PlayerDao

    companion object {
        @Volatile
        private var INSTANCE: MentalArithmeticDB? = null

        fun getDatabase(context: Context): MentalArithmeticDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MentalArithmeticDB::class.java,
                    "jhw_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
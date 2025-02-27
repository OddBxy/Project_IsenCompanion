package fr.isen.LANIER.isensmartcompanion.models

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import fr.isen.LANIER.isensmartcompanion.services.ChatHistoryDao

@Database(
    entities = [ChatMessage::class],
    version = 5,
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun chatHistoryDao() : ChatHistoryDao
}

package fr.isen.LANIER.isensmartcompanion.services

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import fr.isen.LANIER.isensmartcompanion.models.ChatMessage

@Dao
interface ChatHistoryDao {
    @Query("SELECT * FROM chatmessage")
    suspend fun getAll(): List<ChatMessage>

    @Insert
    suspend fun insert(vararg chatMessage: ChatMessage)

    @Delete
    suspend fun deleteOne(vararg chatMessage: ChatMessage)
}
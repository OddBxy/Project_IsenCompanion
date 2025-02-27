package fr.isen.LANIER.isensmartcompanion.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "message") val message : String,
    @ColumnInfo(name = "isFromUser") val isFromUser : Boolean,
    @ColumnInfo(name = "date") val date : String
)

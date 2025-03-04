package fr.isen.LANIER.isensmartcompanion.models

data class IsenCour(
    val title : String,
    val teacher : String,
    val room : String,
    val date : String,
    val hourStart : Int,
    val hourEnd : Int
)

package fr.isen.LANIER.isensmartcompanion.models

import java.io.Serializable

data class IsenEvent(
    val id:String,
    val title:String,
    val description:String,
    val date:String,
    val location:String,
    val category: String
) : Serializable

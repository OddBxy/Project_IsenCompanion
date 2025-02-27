package fr.isen.LANIER.isensmartcompanion.services

import fr.isen.LANIER.isensmartcompanion.models.IsenEvent
import retrofit2.Call
import retrofit2.http.GET

interface EventsService {
    @GET("/events.json")
    fun getEvents(): Call<List<IsenEvent>>

}
package fr.isen.LANIER.isensmartcompanion.services

import android.util.Log
import fr.isen.LANIER.isensmartcompanion.models.IsenEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EventFetcher {
    val events_url = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/events.json/"
    val api = Retrofit.Builder()
        .baseUrl(events_url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(EventsService::class.java)

    fun getAllEvents(retrievCallback : (List<IsenEvent>) -> Unit){

        var events = listOf<IsenEvent>()
        api.getEvents().enqueue(object : Callback<List<IsenEvent>> {
            override fun onResponse(call: Call<List<IsenEvent>>, res: Response<List<IsenEvent>>) {
                if(res.isSuccessful){
                    events = res.body()!!
                    Log.i("CHECL", "onResponse: $events")

                    retrievCallback(events)
                }
            }

            override fun onFailure(p0: Call<List<IsenEvent>>, t: Throwable) {
                Log.i("CHECK", "onFailure: ${t.message}")

                retrievCallback(events)
            }

        })

    }
}
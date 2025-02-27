package fr.isen.LANIER.isensmartcompanion.views

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.isen.LANIER.isensmartcompanion.EventActivity
import fr.isen.LANIER.isensmartcompanion.services.EventsService
import fr.isen.LANIER.isensmartcompanion.models.IsenEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun EventScreen(mod: Modifier){
    val events_url = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/events.json/"

    var events = remember { mutableStateOf(listOf<IsenEvent>()) }
    val api = Retrofit.Builder().baseUrl(events_url).addConverterFactory(GsonConverterFactory.create()).build().create(
        EventsService::class.java)
    api.getEvents().enqueue(object : Callback<List<IsenEvent>>{
        override fun onResponse(call: Call<List<IsenEvent>>, res: Response<List<IsenEvent>>) {
            if(res.isSuccessful){
                events.value = res.body()!!
                Log.i("CHECL", "onResponse: $events")
            }
        }

        override fun onFailure(p0: Call<List<IsenEvent>>, t: Throwable) {
            Log.i("CHECK", "onFailure: ${t.message}")
        }

    })

    if(events.value.isEmpty()){
        Column(
            modifier = mod.fillMaxSize().padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Problem encountered while loading events",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
    else{
        LazyColumn(modifier = mod) {
            items(events.value){
                displayEvent(it)
            }
        }
    }


}

@Composable
fun displayEvent(event: IsenEvent){
    val context = LocalContext.current
    var intent = Intent(context, EventActivity::class.java)
    Card(
        onClick = {
            intent.putExtra("event", event)
            context.startActivity(intent)
        },
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        border = BorderStroke(1.dp, Color.Black),

    ) {
        Column (Modifier.padding(5.dp)){
            Text(event.title)
            Text("Location : ${event.location}")
            Text("Date : ${event.date}")
        }
    }

}
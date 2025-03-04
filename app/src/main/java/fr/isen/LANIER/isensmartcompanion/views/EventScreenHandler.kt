package fr.isen.LANIER.isensmartcompanion.views

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.isen.LANIER.isensmartcompanion.EventActivity
import fr.isen.LANIER.isensmartcompanion.models.IsenEvent
import fr.isen.LANIER.isensmartcompanion.services.EventFetcher
import fr.isen.LANIER.isensmartcompanion.services.NotificationSender
import fr.isen.LANIER.isensmartcompanion.services.EventsService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun EventScreen(mod: Modifier){
    var events by remember { mutableStateOf(listOf<IsenEvent>()) }

    //use singleton to get all events
    EventFetcher.getAllEvents { events = it }



    if(events.isEmpty()){
        Column(
            modifier = mod
                .fillMaxSize()
                .padding(40.dp),
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
            items(events){
                displayEvent(it)
            }
        }
    }


}

@Composable
fun displayEvent(event: IsenEvent){
    val context = LocalContext.current
    var intent = Intent(context, EventActivity::class.java)
    val sharedPreferences = context.getSharedPreferences("eventsNotifier", Context.MODE_PRIVATE)
    var isNotified by remember { mutableStateOf(sharedPreferences.contains(event.title) ?: false) }

    Card(
        onClick = {
            intent.putExtra("event", event)
            context.startActivity(intent)
        },
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
            
    ) {
        Row (verticalAlignment = Alignment.CenterVertically){
            Column (
                Modifier
                    .width(275.dp)
                    .padding(10.dp)
            ){
                Text(event.title)
                Text("Location : ${event.location}")
                Text("Date : ${event.date}")
            }

            FloatingActionButton(
                onClick = {
                    if(isNotified == true){
                        sharedPreferences.edit().remove(event.title).apply()
                        isNotified = false
                        NotificationSender.cancelNotification()
                    }else{
                        sharedPreferences.edit().putBoolean(event.title, true).apply()
                        isNotified = true
                        NotificationSender.sendNotification(context, event.title, event.description, 10000)  //sending a notification
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                //change icon
                if (isNotified == true){
                    Icon(Icons.Filled.Check, "notificationIcon")
                }
                else{
                    Icon(Icons.Filled.Notifications, "notificationIcon")
                }

            }
        }
    }

}
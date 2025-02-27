package fr.isen.LANIER.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.LANIER.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import fr.isen.LANIER.isensmartcompanion.models.IsenEvent

class EventActivity(): ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myEvent = intent.getSerializableExtra("event") as? IsenEvent
        enableEdgeToEdge()
        setContent {
            ISENSmartCompanionTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)
                ) { innerPadding ->
                    displayDetailledEvent(myEvent!!)
                }
            }
        }
    }
}

@Composable
fun displayDetailledEvent(event: IsenEvent){
    Column(
        Modifier.fillMaxSize().padding(15.dp)
    ){
        Text(text = "Title : ${event.title}")
        Text("Location : ${event.location}")
        Text("Date : ${event.date}\n")
        Text("Category : ${event.category}\n")
        Text("Description :\n ${event.description}")

    }
}
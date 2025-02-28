package fr.isen.LANIER.isensmartcompanion.views

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.LANIER.isensmartcompanion.models.AppDataBase
import fr.isen.LANIER.isensmartcompanion.models.ChatMessage
import fr.isen.LANIER.isensmartcompanion.models.GoogleIA
import fr.isen.LANIER.isensmartcompanion.views.components.displayResponse
import fr.isen.LANIER.isensmartcompanion.views.components.displayUserPrompt
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun requestField(chatMessages : MutableList<String>, db : AppDataBase){
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val dao = db.chatHistoryDao()

    val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = dateFormat.format(Date())

    Row(modifier = Modifier.fillMaxWidth().background(color = Color.Transparent).padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween)
    {
        var userPrompt by remember { mutableStateOf("") }
        TextField(
            value = userPrompt,
            onValueChange = {userPrompt = it},
            placeholder = { Text("ask something !") },
            modifier = Modifier.width(275.dp).verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(percent = 20),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )
        FloatingActionButton(
            onClick = {
                chatMessages.add(userPrompt)
                val toast = Toast.makeText(context, "message submitted", Toast.LENGTH_SHORT)
                toast.show()
                coroutine.launch {
                    val inference = GoogleIA.sendPrompt(userPrompt)
                    chatMessages.add(inference)
                    dao.insert(ChatMessage(message = userPrompt, isFromUser = true, date = currentDate))
                    dao.insert(ChatMessage(message = inference, isFromUser = false, date = currentDate))
                    userPrompt = ""
                }
            },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Send, "")
        }
    }
}



@Composable
fun chatDisplay(mod: Modifier, db : AppDataBase){
    var chatMessages by remember { mutableStateOf(mutableStateListOf<String>()) }

    Scaffold(
        modifier = mod,
        bottomBar = { requestField(chatMessages, db) }
    ) { innerPadding ->
        LazyColumn(
            Modifier.padding(innerPadding),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(chatMessages){ index, message ->
                if((index%2) == 0){
                    displayUserPrompt(message)
                }
                else{
                    displayResponse(message)
                }
            }
        }
    }
}


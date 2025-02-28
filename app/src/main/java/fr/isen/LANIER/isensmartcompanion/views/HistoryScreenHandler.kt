package fr.isen.LANIER.isensmartcompanion.views

import android.provider.CalendarContract.Colors
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.LANIER.isensmartcompanion.models.AppDataBase
import fr.isen.LANIER.isensmartcompanion.models.ChatMessage
import fr.isen.LANIER.isensmartcompanion.models.GoogleIA
import fr.isen.LANIER.isensmartcompanion.services.ChatHistoryDao
import fr.isen.LANIER.isensmartcompanion.views.components.displayResponse
import fr.isen.LANIER.isensmartcompanion.views.components.displayUserPrompt
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(mod : Modifier,db : AppDataBase){
    val dao = db.chatHistoryDao()
    val coroutine = rememberCoroutineScope()

    Scaffold(
        modifier = mod,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutine.launch {
                        dao.deleteAll()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Delete, "deleteHistoryButton")
            }
        }
    ) { innerPadding ->
        displayChatHistory(Modifier.padding(innerPadding), dao)
    }

}

@Composable
fun displayChatHistory(mod : Modifier, dao : ChatHistoryDao){
    var messageHistory = remember {mutableStateOf( listOf<ChatMessage>() )}
    val coroutine = rememberCoroutineScope()

    LazyColumn(
        modifier = mod,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        coroutine.launch {
            messageHistory.value = dao.getAll()
            Log.i("CHECKDB", "$messageHistory")

        }
        itemsIndexed(messageHistory.value){ index, chatMessage ->
            if(chatMessage.isFromUser){
                displayUserPrompt(chatMessage.message)
            }
            else{
                displayResponse(chatMessage.message)
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "delete",
                    Modifier.clickable(enabled = true){
                        coroutine.launch {
                            if(chatMessage.isFromUser){
                                dao.deleteOne(messageHistory.value.get(index))
                                dao.deleteOne(messageHistory.value.get(index + 1))
                            }
                            else{
                                dao.deleteOne(messageHistory.value.get(index))
                                dao.deleteOne(messageHistory.value.get(index - 1))
                            }
                        }
                    },
                    textDecoration = TextDecoration.Underline
                )
                Text(chatMessage.date)
            }
        }

    }

}
package fr.isen.LANIER.isensmartcompanion.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import fr.isen.LANIER.isensmartcompanion.models.AppDataBase
import fr.isen.LANIER.isensmartcompanion.models.ChatMessage
import fr.isen.LANIER.isensmartcompanion.services.ChatHistoryDao
import fr.isen.LANIER.isensmartcompanion.views.components.displayResponse
import fr.isen.LANIER.isensmartcompanion.views.components.displayUserPrompt
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(mod : Modifier,db : AppDataBase){
    val dao = db.chatHistoryDao()
    val coroutine = rememberCoroutineScope()
    var messageHistory by remember {mutableStateOf( listOf<ChatMessage>() )}

    LaunchedEffect(Unit) {
        messageHistory = dao.getAll()
        Log.i("CHECKDB", "$messageHistory")
    }

    Scaffold(
        modifier = mod,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutine.launch {
                        dao.deleteAll()
                        messageHistory = dao.getAll()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Delete, "deleteHistoryButton")
            }
        }
    ) { innerPadding ->



        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            itemsIndexed(messageHistory){ index, chatMessage ->
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
                                    dao.deleteOne(messageHistory.get(index))
                                    dao.deleteOne(messageHistory.get(index + 1))
                                }
                                else{
                                    dao.deleteOne(messageHistory.get(index))
                                    dao.deleteOne(messageHistory.get(index - 1))
                                }
                                messageHistory = dao.getAll()
                            }
                        },
                        textDecoration = TextDecoration.Underline
                    )
                    Text(chatMessage.date)
                }
            }

        }
    }

}

package fr.isen.LANIER.isensmartcompanion.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun displayUserPrompt(prompt: String){
    Card(
        Modifier
            .fillMaxWidth()
    ) {
        Column (Modifier.background(MaterialTheme.colorScheme.primary).fillMaxWidth().padding(10.dp)){
            Text("You : $prompt", color = MaterialTheme.colorScheme.background)
        }
    }

}

@Composable
fun displayResponse(response: String){
    Card(
        Modifier.fillMaxWidth()
    ) {
        Column (Modifier.background(MaterialTheme.colorScheme.onSecondary).fillMaxWidth().padding(10.dp)){
            Text("Gemini : $response")
        }
    }

}
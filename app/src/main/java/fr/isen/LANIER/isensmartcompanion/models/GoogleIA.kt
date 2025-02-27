package fr.isen.LANIER.isensmartcompanion.models

import com.google.ai.client.generativeai.GenerativeModel


object GoogleIA {
    //val ak = "AIzaSyDivbULjeNPWm6s-48YtUHT3pCCs7syzWs"
    val generativeModel = GenerativeModel("gemini-1.5-flash", "AIzaSyDivbULjeNPWm6s-48YtUHT3pCCs7syzWs")


    suspend fun sendPrompt(prompt: String): String {

        var response = String()
        try {
            val body = generativeModel.generateContent(prompt)
            response = body.text.toString()
        }
        catch (err : Exception){
            response = "Gemini AI couldn't answer to this"
        }

        return response
    }

}
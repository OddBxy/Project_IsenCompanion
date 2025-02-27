package fr.isen.LANIER.isensmartcompanion.models


import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.LANIER.isensmartcompanion.BuildConfig

object GoogleIA {

    val key = BuildConfig.API_KEY
    val generativeModel = GenerativeModel("gemini-1.5-flash", key)

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
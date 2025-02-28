package fr.isen.LANIER.isensmartcompanion.models

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.isen.LANIER.isensmartcompanion.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object notificationSender {
    //singleton which handles sending notification

    //id is remembered since its in a singleton
    var id = 0

    var notificationCoroutine : Job? = null

    //function needs to be suspend to delay the notification
    fun sendNotification(context : Context, title : String, description: String, delay : Long){

        var notificationBuilder = NotificationCompat.Builder(context, "EVENTS")
            .setSmallIcon(R.drawable.test)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(description))

        with(NotificationManagerCompat.from(context)){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notificationCoroutine = CoroutineScope(Dispatchers.Main).launch {
                delay(delay)
                notify(id, notificationBuilder.build())
            }

            //updating id and lastRunnable
            id+=1
        }
    }

    fun cancelNotification(){
        if(notificationCoroutine != null){
            notificationCoroutine!!.cancel()
        }
    }
}
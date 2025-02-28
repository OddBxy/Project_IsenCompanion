package fr.isen.LANIER.isensmartcompanion.models

import android.content.ClipDescription
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.isen.LANIER.isensmartcompanion.R
import kotlinx.coroutines.delay

object notificationSender {

    //singleton which handles sending notification

    //id is remembered since its in a singleton
    var id = 0

    //function needs to be suspend to delay the notification
    suspend fun sendNotification(context : Context, title : String, description: String, delay : Long){

        var notificationBuilder = NotificationCompat.Builder(context, "EVENTS")
            .setSmallIcon(R.drawable.test)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            //sending notification after a delay
            delay(delay)
            notify(id, notificationBuilder.build())

            //updating id
            id+=1
        }
    }
}
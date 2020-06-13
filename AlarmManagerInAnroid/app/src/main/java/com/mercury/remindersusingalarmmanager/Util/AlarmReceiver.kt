package com.mercury.remindersusingalarmmanager.Util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.mercury.remindersusingalarmmanager.R


/**
 * Created by Himshikhar Gayan.
 */

class AlarmReceiver : BroadcastReceiver() {
    private val local_notification_channel_id = "local_notification"
    private val notification_title = "Alarm fired!"
    private val notification_content = "Alarm has been fired."

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                local_notification_channel_id,
                "local_channel", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "Local Notification"
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val collapsedView = RemoteViews(context.packageName, R.layout.notification_heads_up)
        collapsedView.setTextViewText(R.id.collapsedTitle,notification_title)
        collapsedView.setTextViewText(R.id.collapsedBody,notification_content)

        val notificationBuilder = NotificationCompat.Builder(context,
            local_notification_channel_id
        )   .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(notification_title)
            .setContentText(notification_content)
            .setChannelId(local_notification_channel_id)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
        val notification = notificationBuilder.build()
        notificationManager.notify(200,notification)
    }

}
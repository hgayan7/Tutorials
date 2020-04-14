package com.mercury.notifications.Helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.mercury.notifications.R
import com.mercury.notifications.View.MainActivity

class Util {
    companion object{
        var isOnlyText = false
        private val local_notification_channel_id = "local_notification"
        private var bitmap: Bitmap? = null

        fun AppCompatActivity.showLocalNotification(title : String, body : String) {
            val intent = Intent(this,
                MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this,200,intent, PendingIntent.FLAG_ONE_SHOT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val notificationChannel = NotificationChannel(
                    local_notification_channel_id,
                    "local_channel", NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.description = "Local Notification"
                notificationManager.createNotificationChannel(notificationChannel)
            }
            val collapsedView = RemoteViews(packageName, R.layout.notification_heads_up)
            collapsedView.setTextViewText(R.id.collapsedTitle,title)
            collapsedView.setTextViewText(R.id.collapsedBody,body)

            val notificationBuilder = NotificationCompat.Builder(baseContext,
                local_notification_channel_id
            )   .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(body)
                .setCustomHeadsUpContentView(collapsedView)
                .setChannelId(local_notification_channel_id)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
            if(!isOnlyText){
                notificationBuilder.setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(BitmapFactory.decodeResource(resources,
                        R.drawable.notificationimage
                    )))
            }
            val notification = notificationBuilder.build()
            notificationManager.notify(200,notification)
        }
    }
}
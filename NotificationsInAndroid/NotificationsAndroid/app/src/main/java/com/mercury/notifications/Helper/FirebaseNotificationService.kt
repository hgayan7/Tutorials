package com.mercury.notifications.Helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_SOUND
import androidx.core.app.NotificationCompat.DEFAULT_VIBRATE
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mercury.notifications.R
import com.mercury.notifications.View.MainActivity

class FirebaseNotificationService : FirebaseMessagingService(){

    val CHANNEL_ID = "firebase_notification"
    private var bitmap: Bitmap? = null

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(title = remoteMessage.notification?.title,body = remoteMessage.notification?.body
            ,imageUrl = remoteMessage.notification?.imageUrl)
    }

    private fun sendNotification(title : String?, body : String?, imageUrl : Uri?){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,100,intent,PendingIntent.FLAG_ONE_SHOT)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID,"firebase_channel", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "Firebase Notification"
            notificationChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        bitmap = Glide.with(applicationContext).asBitmap().load(imageUrl).submit().get()

        val collapsedView = RemoteViews(packageName, R.layout.notification_heads_up)
        collapsedView.setTextViewText(R.id.collapsedTitle,title)
        collapsedView.setTextViewText(R.id.collapsedBody,body)

        val notificationBuilder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentText(body)
            .setContentTitle(title)
            .setCustomHeadsUpContentView(collapsedView)
            .setChannelId(CHANNEL_ID)
            .setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
        if(!Util.isOnlyText){
            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
        }
        val notification = notificationBuilder.build()
        notificationManager.notify(100,notification)
    }
}
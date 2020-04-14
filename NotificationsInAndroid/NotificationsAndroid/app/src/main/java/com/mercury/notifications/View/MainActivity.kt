package com.mercury.notifications.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mercury.notifications.Helper.Util
import com.mercury.notifications.ViewModel.NotificationViewModel
import com.mercury.notifications.R
import com.mercury.notifications.Helper.Util.Companion.showLocalNotification
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        setUI()
    }

    private fun setUI() {
        triggerFirebaseNotificationImageButton.setOnClickListener {
            Util.isOnlyText = false
            notificationViewModel.addDataToFirebase(
                title = notificationTitle.text.toString(),
                body = notificationBody.text.toString())
        }
        triggerFirebaseNotificationButton.setOnClickListener {
            Util.isOnlyText = true
            notificationViewModel.addDataToFirebase(
                title = notificationTitle.text.toString(),
                body = notificationBody.text.toString())
        }
        triggerLocalNotificationImageButton.setOnClickListener {
            Util.isOnlyText = false
            showLocalNotification(title = "Local Notification Title", body = "Local Notification body with image")
        }
        triggerLocalNotificationButton.setOnClickListener {
            Util.isOnlyText = true
            showLocalNotification(title = "Local Notification Title", body = "Local Notification body without image")
        }
    }
}

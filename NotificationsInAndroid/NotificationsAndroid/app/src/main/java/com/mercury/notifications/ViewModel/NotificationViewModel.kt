package com.mercury.notifications.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.mercury.notifications.Model.CustomNotification

class NotificationViewModel : ViewModel() {
    private var rootReference = FirebaseDatabase.getInstance().reference

    fun addDataToFirebase(title : String, body : String) {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener {
            if (!it.isSuccessful) {
                Log.w("FCM", "Failed to get FCM token", it.exception)
                return@OnCompleteListener
            }
            val pushID = rootReference.push().key
            val notification =
                CustomNotification(
                    id = pushID ?: "NA", title = title, body = body, token = it.result?.token ?: ""
                )
            rootReference.child("notifications").child(pushID?:"NA").setValue(notification)
        })
    }
}
package com.mercury.remindersusingalarmmanager.ViewModel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.mercury.remindersusingalarmmanager.Util.AlarmReceiver
import java.util.*

class AlarmViewModel(application: Application) : AndroidViewModel(application){

    var alarmManager  = application.applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    private var intent = Intent(application.applicationContext,AlarmReceiver::class.java)
    var pendingIntent = PendingIntent.getBroadcast(application.applicationContext,200,intent,0)

    @RequiresApi(Build.VERSION_CODES.M)
    fun setAlarm(hour: Int, minute: Int) {
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
        )
    }

    fun cancelAlarm() {
        alarmManager?.cancel(this.pendingIntent)
    }
}
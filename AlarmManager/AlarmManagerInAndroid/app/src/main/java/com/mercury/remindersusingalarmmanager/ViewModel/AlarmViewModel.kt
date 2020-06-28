package com.mercury.remindersusingalarmmanager.ViewModel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mercury.remindersusingalarmmanager.Database.Alarm
import com.mercury.remindersusingalarmmanager.Database.AlarmDatabase
import com.mercury.remindersusingalarmmanager.Util.AlarmReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AlarmViewModel(application: Application) : AndroidViewModel(application){

    var alarmManager  = application.applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    private var intent = Intent(application.applicationContext,AlarmReceiver::class.java)
    private var alarmDatabase: AlarmDatabase = AlarmDatabase.getInstance(context = this.getApplication())
    val alarmData : LiveData<List<Alarm>> = alarmDatabase.alarmDAO().getAllAlarms()

    @RequiresApi(Build.VERSION_CODES.M)
    fun setAlarm(hour: Int, minute: Int) {
        val requestCode = (hour.toString() + minute.toString()).toInt()
        val alarm = Alarm(timing = "${hour}:${minute}",requestCode = requestCode)
        insertAlarm(alarm)
        var pendingIntent = PendingIntent.getBroadcast(this.getApplication(),requestCode,intent,0)
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
        )
    }

    fun insertAlarm(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        alarmDatabase.alarmDAO().insertAlarm(alarm)
    }
}
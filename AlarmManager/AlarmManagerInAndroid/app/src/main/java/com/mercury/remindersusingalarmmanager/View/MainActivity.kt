package com.mercury.remindersusingalarmmanager.View

import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.mercury.remindersusingalarmmanager.R
import com.mercury.remindersusingalarmmanager.ViewModel.AlarmViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var alarmViewModel: AlarmViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel::class.java)
        setAlarmButton.setOnClickListener {
            showTimePicker()
        }
        cancelAlarmButton.setOnClickListener {
            alarmViewModel.cancelAlarm()
            Toast.makeText(applicationContext, "Alarm has been cancelled.", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        var picker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            alarmViewModel.setAlarm(hour = hourOfDay, minute = minute)
            Toast.makeText(applicationContext, "Alarm has been set.", Toast.LENGTH_LONG).show()
        }
        //24 hour clock - false
        TimePickerDialog(this,picker,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show()
    }
}
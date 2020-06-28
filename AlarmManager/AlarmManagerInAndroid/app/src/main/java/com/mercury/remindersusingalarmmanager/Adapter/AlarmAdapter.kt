package com.mercury.remindersusingalarmmanager.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mercury.remindersusingalarmmanager.Database.Alarm
import com.mercury.remindersusingalarmmanager.R
import kotlinx.android.synthetic.main.alarm_cell.view.*


/**
 * Created by Himshikhar Gayan.
 */
class AlarmAdapter(private val alarms : List<Alarm>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.alarm_cell, parent, false)
        return AlarmHolder(v)}

    override fun getItemCount(): Int {
        return alarms.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is AlarmHolder ->{
                holder.bind(alarms[position])
            }
        }
    }

    class AlarmHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(alarm : Alarm){
            itemView.alarmTime.text = alarm.timing.toString()
        }
    }
}
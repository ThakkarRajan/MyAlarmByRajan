package com.example.MyAlarmByRajan

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.myalarmbyrajan.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private  lateinit var picker: MaterialTimePicker
    lateinit var binding: ActivityMainBinding
    private  lateinit var  calender : Calendar
    private  lateinit var  alarmManager : AlarmManager
private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

        binding.SelectTime.setOnClickListener {
            shoeTimePicker()
        }

        binding.SetAlarm.setOnClickListener {
        showTime()
        }

        binding.CancelAlarm.setOnClickListener {
        cancelAlarm()
        }
        }

    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast( this,0,intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(this,"Alarm Cancelled",Toast.LENGTH_LONG).show()



    }

    private fun showTime() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast( this,0,intent, PendingIntent.FLAG_IMMUTABLE)


        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,calender.timeInMillis,
            AlarmManager.INTERVAL_DAY,pendingIntent
        )
        Toast.makeText(this,"Alarm Set Successfully",Toast.LENGTH_SHORT).show()



    }

    private fun shoeTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Time")
            .build()
        picker.show(supportFragmentManager,"RajanAndroid")
        picker.addOnPositiveButtonClickListener {
            if(picker.hour > 12){
                binding.SelectTimeText.text =
                    String.format("%02d", abs(picker.hour - 12)) + " : "  + String.format(
                        "%02d", picker.minute
                    ) + "PM"
            }else{
                binding.SelectTimeText.text =
                String.format("%02d",abs(picker.hour)) + " : "  + String.format(
                    "%02d", picker.minute
                ) + "AM"
            }
            calender = Calendar.getInstance()
            calender[Calendar.HOUR_OF_DAY] = picker.hour
            calender[Calendar.MINUTE] = picker.minute
            calender[Calendar.SECOND] = 0
            calender[Calendar.MILLISECOND] = 0
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence = "RajanAndroidReminderChannel"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel("RajanAndroid",name,importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}

package com.example.baserepository.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import java.util.*


class AlarmService : Service() {
    private val iBinder = AlarmServiceBinder()

    inner class AlarmServiceBinder : Binder() {
        fun getAlarmService() = this@AlarmService
    }

    fun getRandom(context: Context) {
        var i = Random().nextInt(10)
        while (i > 0) {
            i = i.minus(1)
            Thread.sleep(1000)
            if (i == 0) {
                val intent = Intent("com.alarm.service")
                val extras = Bundle()
                extras.putString("Halo", "Bao thuc")
                intent.putExtras(extras)
                context.sendBroadcast(intent)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return iBinder
    }
}

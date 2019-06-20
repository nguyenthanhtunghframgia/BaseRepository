package com.example.baserepository

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.baserepository.service.AlarmService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var alarmService: AlarmService
    private var isConnect: Boolean? = null
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onClick()
        registerReceiver()
    }

    private fun registerReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                Toast.makeText(this@MainActivity, intent?.getStringExtra("Halo"), Toast.LENGTH_LONG).show()
            }
        }

        registerReceiver(broadcastReceiver, IntentFilter("com.alarm.service"))
    }

    private fun onClick() {
        tv.setOnClickListener {
            alarmService.getRandom(this@MainActivity)
        }
    }

    //Bind service to activity
    override fun onStart() {
        super.onStart()
        Intent(this, AlarmService::class.java).also {
            bindService(it, serverConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private val serverConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnect = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AlarmService.AlarmServiceBinder
            alarmService = binder.getAlarmService()
            isConnect = true
        }
    }

    //Unbind service
    override fun onStop() {
        super.onStop()
        unbindService(serverConnection)
        isConnect = false
        //
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
        }
    }
}

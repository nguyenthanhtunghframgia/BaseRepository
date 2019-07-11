package com.example.baserepository

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.baserepository.service.AlarmService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var alarmService: AlarmService
    private var isConnect: Boolean? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    private lateinit var gas: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onClick()
        initAction()
        registerReceiver()
        setUpGesture()
    }

    private fun setUpGesture() {
        gas = GestureDetector(
            this@MainActivity,
            object : GestureDetector.SimpleOnGestureListener() {

                override fun onDown(e: MotionEvent): Boolean {
                    Toast.makeText(this@MainActivity, "onDown", Toast.LENGTH_SHORT).show()
                    return true
                }

                override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                    e1?.apply {
                        e2?.apply {
                            if (e1.x < e2.x) {
                                onSwipeRight()
                            }
                            if (e1.x > e2.x) {
                                onSwipeLeft()
                            }
                            if (e1.y < e2.y) {
                                onSwipeDown()
                            }
                            if (e1.y > e2.y) {
                                onSwipeUp()
                            }
                        }
                    }
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })
    }

    private fun onSwipeUp() {
        Toast.makeText(this@MainActivity, "onSwipeUp", Toast.LENGTH_SHORT).show()
    }

    private fun onSwipeDown() {
        Toast.makeText(this@MainActivity, "onSwipeDown", Toast.LENGTH_SHORT).show()
    }

    private fun onSwipeLeft() {
        Toast.makeText(this@MainActivity, "onSwipeLeft", Toast.LENGTH_SHORT).show()
        card1_small.visibility = View.INVISIBLE
        card1_large.visibility = View.VISIBLE
        card2_small.visibility = View.VISIBLE
        card2_large.visibility = View.INVISIBLE
    }

    private fun onSwipeRight() {
        Toast.makeText(this@MainActivity, "onSwipeRight", Toast.LENGTH_LONG).show()
        card2_small.visibility = View.INVISIBLE
        card2_large.visibility = View.VISIBLE
        card1_small.visibility = View.VISIBLE
        card1_large.visibility = View.INVISIBLE
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gas.onTouchEvent(event)
        return true
    }

    private fun initAction() {
        card1_large.setOnClickListener {
            //Todo implement if need
        }

        card1_small.setOnClickListener {
            card1_small.visibility = View.INVISIBLE
            card1_large.visibility = View.VISIBLE
            card2_small.visibility = View.VISIBLE
            card2_large.visibility = View.INVISIBLE
        }

        card2_large.setOnClickListener {
            //Todo implement if need
        }

        card2_small.setOnClickListener {
            card2_small.visibility = View.INVISIBLE
            card2_large.visibility = View.VISIBLE
            card1_small.visibility = View.VISIBLE
            card1_large.visibility = View.INVISIBLE
        }
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

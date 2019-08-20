package com.example.baserepository

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.baserepository.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        initProgress()
    }

    private fun initProgress() {
        binding.apply {
            btnStartProgressWheel.setOnClickListener {
//                var i = 1
//                object : CountDownTimer(101000, 1000) {
//                    override fun onFinish() {
//                    }
//
//                    override fun onTick(millisUntilFinished: Long) {
//                        if (i <= 100) {
//                            progressWheel.setProgress(i)
//                            i++
//                        }
//                    }
//                }.start()
                progressWheel.autoSpinWithAnim()
            }
        }
    }
}

package com.example.ronbrosh.rocketlauncher.splash.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.view.RocketListActivity
import com.example.ronbrosh.rocketlauncher.utils.AppUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AppUtil.runOnMainThreadAfterDelay(2000) {
            val intent = Intent(this, RocketListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
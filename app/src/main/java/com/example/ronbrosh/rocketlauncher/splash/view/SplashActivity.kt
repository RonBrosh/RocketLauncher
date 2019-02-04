package com.example.ronbrosh.rocketlauncher.splash.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.rocketlauncher.view.RocketLauncherActivity
import com.example.ronbrosh.rocketlauncher.utils.AppUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AppUtil.hideSystemUI(window)
        AppUtil.runOnMainThreadAfterDelay(2000) {
            val intent = Intent(this, RocketLauncherActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
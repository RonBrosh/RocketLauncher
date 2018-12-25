package com.example.ronbrosh.rocketlauncher.rocketlauncher

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.view.RocketListFragment

class RocketLauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_launcher)
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, RocketListFragment.newInstance(), RocketListFragment.TAG).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onBackPressed()
    }
}
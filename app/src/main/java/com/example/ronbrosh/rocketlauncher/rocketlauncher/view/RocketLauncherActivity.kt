package com.example.ronbrosh.rocketlauncher.rocketlauncher.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view.RocketDetailsFragment

class RocketLauncherActivity : AppCompatActivity() {
    private lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_launcher)

        // Init tool bar.
        toolBar = findViewById(R.id.toolBar)
        toolBar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun showRocketDetails(rocket: Rocket) {
        toolBar.title = rocket.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, RocketDetailsFragment.getInstance(rocket.rocketId)).addToBackStack(null).commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        toolBar.title = getString(R.string.rocket_list_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
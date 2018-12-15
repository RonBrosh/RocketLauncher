package com.example.ronbrosh.rocketlauncher.rocketdetails.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ronbrosh.rocketlauncher.R
import android.content.Intent
import com.example.ronbrosh.rocketlauncher.utils.Constants


class RocketDetailsActivity : AppCompatActivity() {
    companion object {
        private const val INTENT_EXTRA_ROCKET_ID: String = "INTENT_EXTRA_ROCKET_ID"
        fun newIntent(context: Context, rocketId: Long): Intent {
            val intent = Intent(context, RocketDetailsActivity::class.java)
            intent.putExtra(RocketDetailsActivity.INTENT_EXTRA_ROCKET_ID, rocketId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_details)

        val rocketId: Long = intent.getLongExtra(INTENT_EXTRA_ROCKET_ID, Constants.NO_LONG_VALUE)
        if (rocketId != Constants.NO_LONG_VALUE) {
            supportActionBar?.title = "Rocket id = $rocketId"
        }
    }
}
package com.example.ronbrosh.rocketlauncher.rocketdetails.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.api.RocketApi
import com.example.ronbrosh.rocketlauncher.model.Launch
import com.example.ronbrosh.rocketlauncher.rocketdetails.view.model.RocketDetailsViewModel
import com.example.ronbrosh.rocketlauncher.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RocketDetailsActivity : AppCompatActivity() {
    private lateinit var rocketDetailsViewModel: RocketDetailsViewModel

    companion object {
        private const val INTENT_EXTRA_ROCKET_ID: String = "INTENT_EXTRA_ROCKET_ID"
        private const val INTENT_EXTRA_ROCKET_NAME: String = "INTENT_EXTRA_ROCKET_NAME"

        fun newIntent(context: Context, rocketId: Long, rocketName: String): Intent {
            val intent = Intent(context, RocketDetailsActivity::class.java)
            intent.putExtra(RocketDetailsActivity.INTENT_EXTRA_ROCKET_ID, rocketId)
            intent.putExtra(RocketDetailsActivity.INTENT_EXTRA_ROCKET_NAME, rocketName)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_details)

        // Get the selected rocket id.
        val rocketId: Long = intent.getLongExtra(INTENT_EXTRA_ROCKET_ID, Constants.NO_LONG_VALUE)
        if (rocketId == Constants.NO_LONG_VALUE) {
            // No rocket selected. maybe show empty details?
            return
        }

        // Set action bar title.
        val rocketName: String = intent.getStringExtra(INTENT_EXTRA_ROCKET_NAME)
        rocketName.let { title ->
            supportActionBar?.let { actionBar ->
                actionBar.title = title
            }
        }

        // Init view model.
        rocketDetailsViewModel = RocketDetailsViewModel(application, rocketId)
        rocketDetailsViewModel.getRocketWithLaunchListLiveData().observe(this, Observer {
            if (it.launchList.isEmpty()) {
                rocketDetailsViewModel.fetchRocketLaunchList(rocketId)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
package com.example.ronbrosh.rocketlauncher.rocketdetails.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.api.RocketApi
import com.example.ronbrosh.rocketlauncher.model.Launch
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            val rocketId: Long = intent.getLongExtra(INTENT_EXTRA_ROCKET_ID, Constants.NO_LONG_VALUE)
            if (rocketId != Constants.NO_LONG_VALUE) {
                it.title = "Rocket id = $rocketId"
                RocketApi.Factory.getInstance().fetchRocketLaunchesList(rocketId).enqueue(object : Callback<List<Launch>> {
                    override fun onFailure(call: Call<List<Launch>>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<List<Launch>>, response: Response<List<Launch>>) {
                        response.body()?.let { result ->
                            Log.d("Test", result.size.toString())
                        }
                    }
                })
            }
        }
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
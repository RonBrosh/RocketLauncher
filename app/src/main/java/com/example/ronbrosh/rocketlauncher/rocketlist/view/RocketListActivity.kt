package com.example.ronbrosh.rocketlauncher.rocketlist.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.db.RocketData
import com.example.ronbrosh.rocketlauncher.rocketlist.model.RocketListViewModel

class RocketListActivity : AppCompatActivity(), RocketListItemClickListener {
    private lateinit var rocketListViewModel: RocketListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var rocketListAdapter: RocketListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_list)

        // Init recycler view.
        recyclerView = findViewById(R.id.recyclerViewRocketData)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rocketListAdapter = RocketListAdapter()
        rocketListAdapter.setRocketListItemClickListener(this)
        recyclerView.adapter = rocketListAdapter

        // Init view model.
        rocketListViewModel = RocketListViewModel(application)
        rocketListViewModel.getAllRocketData().observe(this, Observer {
            if (it.isEmpty())
                rocketListViewModel.fetchRocketDataFromServer()
            else {
                rocketListAdapter.submitList(it)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        rocketListViewModel.deleteAllRocketData()
    }

    override fun onRocketItemClick(rocketData: RocketData) {
        Toast.makeText(this, rocketData.name, Toast.LENGTH_SHORT).show()
        var newRocketData: RocketData = rocketData.copy()
        newRocketData.name = "test"
        rocketListViewModel.insertRocketData(newRocketData)
    }
}
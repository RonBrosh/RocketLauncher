package com.example.ronbrosh.rocketlauncher.rocketlist.view

import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.rocketdetails.view.RocketDetailsActivity
import com.example.ronbrosh.rocketlauncher.rocketlist.model.RocketListViewModel


class RocketListActivity : AppCompatActivity(), RocketListItemClickListener, CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var rocketListViewModel: RocketListViewModel
    private lateinit var rocketListAdapter: RocketListAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var switchFilterByActive: Switch
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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

        // Init Switch filter by isActive
        switchFilterByActive = findViewById(R.id.switchFilterByActive)
        switchFilterByActive.setOnCheckedChangeListener(this)

        // Init swipe Refresh Layout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)

        // Init view model.
        rocketListViewModel = RocketListViewModel(application)
        rocketListViewModel.getRocketListLiveData().observe(this, Observer {
            if (it.isEmpty())
                rocketListViewModel.fetchRocketList()
            else {
                recyclerView.scheduleLayoutAnimation()
                rocketListAdapter.submitList(it)
            }
        })

        rocketListViewModel.getLoadingLiveData().observe(this, Observer {
            swipeRefreshLayout.isRefreshing = it
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        rocketListViewModel.deleteRocketTable()
    }

    override fun onRocketItemClick(rocket: Rocket) {
        startActivity(RocketDetailsActivity.newIntent(this, rocket.id))
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        rocketListAdapter.submitList(rocketListViewModel.getFilteredRocketList(isChecked))
    }

    override fun onRefresh() {
        rocketListAdapter.submitList(null)
        rocketListViewModel.fetchRocketList()
    }
}
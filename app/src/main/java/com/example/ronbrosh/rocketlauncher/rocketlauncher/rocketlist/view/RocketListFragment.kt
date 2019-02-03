package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.rocketlauncher.RocketLauncherActivity
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.model.RocketListViewModel

class RocketListFragment : Fragment(), RocketListAdapterListener, CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var switchFilterByActive: Switch
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var rocketListViewModel: RocketListViewModel
    private lateinit var rocketListAdapter: RocketListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_rocket_list, container, false)

        // Init recycler view.
        recyclerView = rootView.findViewById(R.id.recyclerViewRocketData)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rocketListAdapter = RocketListAdapter()
        rocketListAdapter.setRocketListItemClickListener(this)
        recyclerView.adapter = rocketListAdapter

        // Init Switch filter by isActive.
        switchFilterByActive = rootView.findViewById(R.id.switchFilterByActive)
        switchFilterByActive.setOnCheckedChangeListener(this)

        // Init swipe Refresh Layout.
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init view model.
        rocketListViewModel = RocketListViewModel(activity!!.application)
        rocketListViewModel.getRocketListLiveData().observe(this, Observer { list ->
            if (list.isEmpty()) {
                rocketListViewModel.fetchRocketList()
            } else {
                //TODO add delay so this animation will be shown on first launch.
                recyclerView.scheduleLayoutAnimation()
                rocketListAdapter.submitList(list)
            }
        })

        rocketListViewModel.getLoadingLiveData().observe(this, Observer { isLoading ->
            swipeRefreshLayout.isRefreshing = isLoading
        })
    }

    override fun onRocketItemClick(viewHolder: RecyclerView.ViewHolder, rocket: Rocket) {
        (activity as RocketLauncherActivity).showRocketDetails(rocket)
    }

    override fun onLastSelectedItemLoadFinished() {
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (!swipeRefreshLayout.isRefreshing) {
            //TODO scroll to top if not filtering the rocket list.
            rocketListAdapter.submitList(rocketListViewModel.getFilteredRocketList(isChecked))
        }
    }

    override fun onRefresh() {
        switchFilterByActive.isChecked = false
        rocketListAdapter.submitList(null)
        rocketListViewModel.fetchRocketList()
    }
}
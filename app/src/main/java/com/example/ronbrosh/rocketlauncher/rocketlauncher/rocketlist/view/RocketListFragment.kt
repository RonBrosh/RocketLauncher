package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view.RocketDetailsFragment
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.model.RocketListViewModel

class RocketListFragment : Fragment(), RocketListItemClickListener, CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var rocketListViewModel: RocketListViewModel
    private lateinit var rocketListAdapter: RocketListAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var switchFilterByActive: Switch
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    companion object {
        val TAG: String = RocketListFragment::class.java.simpleName
        fun newInstance(): RocketListFragment {
            return RocketListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_rocket_list, container, false)
        // Init recycler view.
        recyclerView = rootView.findViewById(R.id.recyclerViewRocketData)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL, false)
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
        activity?.let { activity ->
            // Set action bar title.
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)

            // Init view model.
            rocketListViewModel = RocketListViewModel(activity.application)
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


    }

    override fun onRocketItemClick(view: View, rocket: Rocket) {
        activity?.let { activity ->
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.setReorderingAllowed(true)
            if (Build.VERSION.SDK_INT >= 21) {
                fragmentTransaction.addSharedElement(view, view.transitionName)
            }

            fragmentTransaction.add(R.id.fragmentContainer, RocketDetailsFragment.newInstance(rocket.rocketId, rocket.name), RocketDetailsFragment.TAG)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (!swipeRefreshLayout.isRefreshing)
            rocketListAdapter.submitList(rocketListViewModel.getFilteredRocketList(isChecked))
    }

    override fun onRefresh() {
        switchFilterByActive.isChecked = false
        rocketListAdapter.submitList(null)
        rocketListViewModel.fetchRocketList()
    }
}
package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Window
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view.RocketDetailsActivity
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.model.RocketListViewModel
import kotlinx.android.synthetic.main.layout_rocket_details.view.*

class RocketListActivity : AppCompatActivity(), RocketListAdapterListener, CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var rocketListViewModel: RocketListViewModel
    private lateinit var rocketListAdapter: RocketListAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var switchFilterByActive: Switch
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var bundle: Bundle? = null
    private var recyclerViewState: Parcelable? = null

    companion object {
        val TAG: String = RocketListActivity::class.java.simpleName
        private const val CURRENT_RECYCLER_VIEW_POSITION: String = "CURRENT_RECYCLER_VIEW_POSITION"
    }

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

        // Init Switch filter by isActive.
        switchFilterByActive = findViewById(R.id.switchFilterByActive)
        switchFilterByActive.setOnCheckedChangeListener(this)

        // Init swipe Refresh Layout.
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)

        // Init view model.
        rocketListViewModel = RocketListViewModel(application)
        rocketListViewModel.getRocketListLiveData().observe(this, Observer {
            if (it.isEmpty())
                rocketListViewModel.fetchRocketList()
            else {
                // Show the animation only when not clicked on item yet,
                // So when returning back to this fragment the transition will be smooth.
                if (bundle == null) {
                    recyclerView.scheduleLayoutAnimation()
                }
                rocketListAdapter.submitList(it)
                recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            }
        })

        rocketListViewModel.getLoadingLiveData().observe(this, Observer {
            swipeRefreshLayout.isRefreshing = it
        })

        // Notify the adapter about selected item position that is later will be used for the transition.
        bundle?.let { bundle ->
            bundle.getInt(CURRENT_RECYCLER_VIEW_POSITION).let { position ->
                rocketListAdapter.setSelectedItemPosition(position)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bundle = Bundle()
        bundle!!.putInt(CURRENT_RECYCLER_VIEW_POSITION, rocketListAdapter.getSelectedItemPosition())

        recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onRocketItemClick(viewHolder: RecyclerView.ViewHolder, rocket: Rocket) {
        val intent = Intent(this, RocketDetailsActivity::class.java)
        intent.putExtra(RocketDetailsActivity.INTENT_EXTRA_ROCKET_ID, rocket.rocketId)
        intent.putExtra(RocketDetailsActivity.INTENT_EXTRA_ROCKET_NAME, rocket.name)
        if (Build.VERSION.SDK_INT >= 21) {
            intent.putExtra(RocketDetailsActivity.INTENT_EXTRA_TRANSITION_NAME, viewHolder.itemView.rocketDetailsContainer.transitionName)
            val activityOptionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    Pair.create(findViewById(android.R.id.statusBarBackground), Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME),
                    Pair.create(findViewById(android.R.id.navigationBarBackground), Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME),
                    Pair.create(findViewById(R.id.toolBar), getString(R.string.tool_bar_transition_name)),
                    Pair.create(viewHolder.itemView.rocketDetailsContainer, viewHolder.itemView.rocketDetailsContainer.transitionName))
            startActivity(intent, activityOptionsCompat.toBundle())
        } else {
            startActivity(intent)
        }
    }

    override fun onLastSelectedItemLoadFinished() {
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (!swipeRefreshLayout.isRefreshing)
            rocketListAdapter.submitList(rocketListViewModel.getFilteredRocketList(isChecked))
    }

    override fun onRefresh() {
        recyclerViewState = null
        bundle = null
        switchFilterByActive.isChecked = false
        rocketListAdapter.submitList(null)
        rocketListViewModel.fetchRocketList()
    }
}
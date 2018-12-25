package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view.RocketDetailsFragment
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.model.RocketListViewModel
import android.os.Parcelable


class RocketListFragment : Fragment(), RocketListAdapterListener, CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var rocketListViewModel: RocketListViewModel
    private lateinit var rocketListAdapter: RocketListAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var switchFilterByActive: Switch
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var bundle: Bundle? = null
    private var recyclerViewState: Parcelable? = null

    companion object {
        val TAG: String = RocketListFragment::class.java.simpleName
        const val CURRENT_RECYCLER_VIEW_POSITION: String = "CURRENT_RECYCLER_VIEW_POSITION"

        fun newInstance(): RocketListFragment {
            return RocketListFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bundle = Bundle()
        bundle!!.putInt(CURRENT_RECYCLER_VIEW_POSITION, rocketListAdapter.getSelectedItemPosition())

        recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
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
        }

        // Scroll to last recycler view position.
        bundle?.let { bundle ->
            bundle.getInt(CURRENT_RECYCLER_VIEW_POSITION).let { position ->
                rocketListAdapter.setSelectedItemPosition(position)
            }
        }

        postponeEnterTransition()
    }

    override fun onRocketItemClick(view: View, rocket: Rocket) {
        activity?.let { activity ->
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.setReorderingAllowed(true)
            if (Build.VERSION.SDK_INT >= 21) {
                ViewCompat.getTransitionName(view)?.let { transitionName ->
                    fragmentTransaction.addSharedElement(view, transitionName)
                    fragmentTransaction.replace(R.id.fragmentContainer, RocketDetailsFragment.newInstance(rocket.rocketId, rocket.name, transitionName), RocketDetailsFragment.TAG)
                }
            } else {
                fragmentTransaction.replace(R.id.fragmentContainer, RocketDetailsFragment.newInstance(rocket.rocketId, rocket.name, ""), RocketDetailsFragment.TAG)
            }

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onLastSelectedItemLoadFinished() {
        startPostponedEnterTransition()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (!swipeRefreshLayout.isRefreshing)
            rocketListAdapter.submitList(rocketListViewModel.getFilteredRocketList(isChecked))
    }

    override fun onRefresh() {
        bundle = null
        switchFilterByActive.isChecked = false
        rocketListAdapter.submitList(null)
        rocketListViewModel.fetchRocketList()
    }

    private fun scrollToPosition(position: Int) {
        recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View,
                                        left: Int,
                                        top: Int,
                                        right: Int,
                                        bottom: Int,
                                        oldLeft: Int,
                                        oldTop: Int,
                                        oldRight: Int,
                                        oldBottom: Int) {
                recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager = recyclerView.layoutManager
                val viewAtPosition = layoutManager?.findViewByPosition(position)
                if (viewAtPosition == null || layoutManager
                                .isViewPartiallyVisible(viewAtPosition, false, true)) {
                    layoutManager?.let {
                        recyclerView.post { it.scrollToPosition(position) }
                    }
                }
            }
        })
    }
}
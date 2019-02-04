package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Launch
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view.model.RocketDetailsViewModel
import com.example.ronbrosh.rocketlauncher.rocketlauncher.view.ImageViewPagerAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_rocket_details.view.*
import java.util.*

class RocketDetailsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var rocketDetailsViewModel: RocketDetailsViewModel? = null
    private lateinit var lineChart: LineChart
    private lateinit var viewPager: ViewPager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var rocketDetailsAdapter: RocketDetailsAdapter
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter

    companion object {
        private const val BUNDLE_EXTRA_ROCKET_ID: String = "BUNDLE_EXTRA_ROCKET_ID"

        fun getInstance(rocketId: String): RocketDetailsFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_EXTRA_ROCKET_ID, rocketId)

            val rocketDetailsFragment = RocketDetailsFragment()
            rocketDetailsFragment.arguments = bundle

            return rocketDetailsFragment
        }
    }

    private fun initLineChart(rootView: View) {
        lineChart = rootView.findViewById(R.id.lineChart)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setNoDataText(null)
        lineChart.setBackgroundColor(Color.WHITE)
        lineChart.setScaleEnabled(false)
        lineChart.setTouchEnabled(false)
        lineChart.setDrawGridBackground(true)
        lineChart.setGridBackgroundColor(if (Build.VERSION.SDK_INT >= 23) lineChart.context.getColor(R.color.lightGray) else resources.getColor(R.color.lightGray))
        lineChart.setNoDataText(getString(R.string.line_chart_no_data_text))
        lineChart.setNoDataTextColor(if (Build.VERSION.SDK_INT >= 23) lineChart.context.getColor(R.color.colorPrimaryDark) else resources.getColor(R.color.colorPrimaryDark))

        lineChart.xAxis.setDrawGridLines(true)
        lineChart.xAxis.granularity = 1.0f
        lineChart.xAxis.isGranularityEnabled = true
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.axisLineWidth = resources.getDimension(R.dimen.line_chart_axis_width)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.axisLineWidth = resources.getDimension(R.dimen.line_chart_axis_width)
        lineChart.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter "" + value.toInt()
        }

        lineChart.axisLeft.granularity = 1.0f
        lineChart.axisLeft.isGranularityEnabled = true
        lineChart.axisLeft.setValueFormatter { value, _ ->
            return@setValueFormatter "" + value.toInt()
        }
    }

    private fun setLineChartData(launchList: List<Launch>) {
        // Calculate launches per year.
        val dataHashMap = HashMap<Int, Int>()
        for (nextLaunch in launchList) {
            val year = nextLaunch.year.toInt()
            var count = 1
            dataHashMap[year]?.let {
                count += it
            }
            dataHashMap[year] = count
        }
        val dataSortedMap = dataHashMap.toSortedMap()

        // Fix x axis labels count.
        lineChart.xAxis.labelCount = dataSortedMap.size

        // Create the data.
        val data: ArrayList<Entry> = ArrayList()
        for (entry in dataSortedMap.entries) {
            data.add(Entry(entry.key.toFloat(), entry.value.toFloat()))
        }

        // Create the data set.
        val lineDataSet = LineDataSet(data, null)
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = if (Build.VERSION.SDK_INT >= 23) lineChart.context.getColor(R.color.colorAccent75) else resources.getColor(R.color.colorAccent75)
        lineDataSet.color = if (Build.VERSION.SDK_INT >= 23) lineChart.context.getColor(R.color.colorPrimary) else resources.getColor(R.color.colorPrimary)
        lineDataSet.valueTextColor = if (Build.VERSION.SDK_INT >= 23) lineChart.context.getColor(R.color.colorPrimaryDark) else resources.getColor(R.color.colorPrimaryDark)
        lineDataSet.lineWidth = resources.getDimension(R.dimen.line_chart_line_width)
        lineDataSet.circleColors = List(1) { if (Build.VERSION.SDK_INT >= 23) lineChart.context.getColor(R.color.colorPrimary) else resources.getColor(R.color.colorPrimary) }
        lineDataSet.circleRadius = resources.getDimension(R.dimen.line_chart_value_circle_radius)
        lineDataSet.circleHoleColor = if (Build.VERSION.SDK_INT >= 23) lineChart.context.getColor(R.color.lightGray) else resources.getColor(R.color.lightGray)
        lineDataSet.circleHoleRadius = resources.getDimension(R.dimen.line_chart_value_circle_hole_radius)
        lineDataSet.valueTextSize = resources.getDimension(R.dimen.text_mini)
        lineDataSet.valueFormatter = IValueFormatter { value: Float, _: Entry, _: Int, _: ViewPortHandler ->
            return@IValueFormatter "" + value.toInt()
        }

        // Set the data to line chart.
        lineChart.data = LineData(lineDataSet)
        lineChart.invalidate()
    }

    private fun initRocketDetails(rocket: Rocket) {
        view?.findViewById<TextView>(R.id.textViewRocketName)?.text = rocket.name
        view?.findViewById<TextView>(R.id.textViewRocketCountry)?.text = rocket.country
        view?.findViewById<TextView>(R.id.textViewRocketEnginesCount)?.text = String.format(getString(R.string.rocket_data_engines_count_format), rocket.engine.enginesCount)

        imageViewPagerAdapter = ImageViewPagerAdapter(rocket.imageUrlList)
        viewPager.adapter = imageViewPagerAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_rocket_details, container, false)

        initLineChart(rootView)

        // Init recycler view.
        recyclerView = rootView.findViewById(R.id.recyclerViewRocketLaunches)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rocketDetailsAdapter = RocketDetailsAdapter()
        recyclerView.adapter = rocketDetailsAdapter

        // Init swipe Refresh Layout.
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)

        // Init view pager.
        viewPager = rootView.findViewById(R.id.viewPager)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(BUNDLE_EXTRA_ROCKET_ID)?.let { rocketId ->
            rocketDetailsViewModel = RocketDetailsViewModel(activity!!.application, rocketId)
            rocketDetailsViewModel?.getRocketWithLaunchListLiveData()?.observe(this, Observer { data ->
                initRocketDetails(data.rocket)

                if (data.launchList.isEmpty()) {
                    rocketDetailsViewModel?.fetchRocketLaunchList(rocketId)
                } else {
                    val launchList: List<Launch> = data.launchList.sortedByDescending { it.year }
                    setLineChartData(launchList)
                    rocketDetailsAdapter.setData(launchList)
                }
            })

            rocketDetailsViewModel?.getLoadingLiveData()?.observe(this, Observer { isLoading ->
                swipeRefreshLayout.isRefreshing = isLoading
            })
        }
    }

    override fun onRefresh() {
        rocketDetailsViewModel?.refresh()
    }
}
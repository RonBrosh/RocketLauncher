package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Launch
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view.model.RocketDetailsViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*

class RocketDetailsFragment : Fragment() {
    private lateinit var rocketDetailsViewModel: RocketDetailsViewModel
    private lateinit var lineChart: LineChart

    companion object {
        val TAG: String = RocketDetailsFragment::class.java.simpleName
        private const val INTENT_EXTRA_ROCKET_ID: String = "INTENT_EXTRA_ROCKET_ID"
        private const val INTENT_EXTRA_ROCKET_NAME: String = "INTENT_EXTRA_ROCKET_NAME"
        private const val INTENT_EXTRA_TRANSITION_NAME: String = "INTENT_EXTRA_TRANSITION_NAME"

        fun newInstance(rocketId: String, rocketName: String, transitionName: String): RocketDetailsFragment {
            val bundle = Bundle()
            bundle.putString(INTENT_EXTRA_ROCKET_ID, rocketId)
            bundle.putString(INTENT_EXTRA_ROCKET_NAME, rocketName)
            bundle.putString(INTENT_EXTRA_TRANSITION_NAME, transitionName)
            val rocketDetailsFragment = RocketDetailsFragment()
            rocketDetailsFragment.arguments = bundle
            return rocketDetailsFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_rocket_details, container, false)
        initLineChart(rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTransitionElement(view)

        arguments?.let { arguments ->
            val rocketId = arguments.getString(INTENT_EXTRA_ROCKET_ID, "")
            val rocketName = arguments.getString(INTENT_EXTRA_ROCKET_NAME, "")

            activity?.let { activity ->
                // Set action bar title.
                (activity as AppCompatActivity).supportActionBar?.let { actionBar ->
                    actionBar.title = rocketName
                    actionBar.setDisplayHomeAsUpEnabled(true)
                }

                // Init view model.
                rocketDetailsViewModel = RocketDetailsViewModel(activity.application, rocketId)
                rocketDetailsViewModel.getRocketWithLaunchListLiveData().observe(this, Observer {
                    initRocketDetails(it.rocket)
                    if (it.launchList.isEmpty()) {
                        rocketDetailsViewModel.fetchRocketLaunchList(rocketId)
                    } else {
                        setLineChartData(it.launchList)
                    }
                })
            }
        }
    }

    private fun initTransitionElement(rootView: View) {
        arguments?.let { arguments ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val transitionName: String = arguments.getString(INTENT_EXTRA_TRANSITION_NAME, "")
                val sharedElement: View = rootView.findViewById<View>(R.id.rocketDetailsContainer)
                sharedElement.transitionName = transitionName
            }
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
        view?.let {
            it.findViewById<TextView>(R.id.textViewRocketName).text = rocket.name
            it.findViewById<TextView>(R.id.textViewRocketCountry).text = rocket.country
            it.findViewById<TextView>(R.id.textViewRocketEnginesCount).text = String.format(getString(R.string.rocket_data_engines_count_format), rocket.engine.enginesCount)
            Picasso.get().load(rocket.imageUrlList[0]).into(it.findViewById<ImageView>(R.id.imageViewPreview), object : Callback {
                override fun onSuccess() {
                    startPostponedEnterTransition()
                }

                override fun onError(e: Exception?) {
                    startPostponedEnterTransition()
                }
            })
        }
    }
}
package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

class RocketDetailsActivity : AppCompatActivity() {
    private lateinit var rocketDetailsViewModel: RocketDetailsViewModel
    private lateinit var lineChart: LineChart

    companion object {
        val TAG: String = RocketDetailsActivity::class.java.simpleName
        const val INTENT_EXTRA_ROCKET_ID: String = "INTENT_EXTRA_ROCKET_ID"
        const val INTENT_EXTRA_ROCKET_NAME: String = "INTENT_EXTRA_ROCKET_NAME"
        const val INTENT_EXTRA_TRANSITION_NAME: String = "INTENT_EXTRA_TRANSITION_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_details)

        initLineChart()

        val rocketId: String? = intent.extras?.getString(INTENT_EXTRA_ROCKET_ID, "")
        val rocketName: String? = intent.extras?.getString(INTENT_EXTRA_ROCKET_NAME, "")

        supportActionBar?.let { supportActionBar ->
            supportActionBar.title = rocketName
            supportActionBar.setDisplayHomeAsUpEnabled(true)
        }

        // Init view model.
        rocketId?.let { rocketId ->
            rocketDetailsViewModel = RocketDetailsViewModel(application, rocketId)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initLineChart() {
        lineChart = findViewById(R.id.lineChart)
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
        findViewById<TextView>(R.id.textViewRocketName).text = rocket.name
        findViewById<TextView>(R.id.textViewRocketCountry).text = rocket.country
        findViewById<TextView>(R.id.textViewRocketEnginesCount).text = String.format(getString(R.string.rocket_data_engines_count_format), rocket.engine.enginesCount)
        Picasso.get().load(rocket.imageUrlList[0]).into(findViewById(R.id.imageViewPreview), object : Callback {
            override fun onSuccess() {
            }

            override fun onError(e: Exception?) {
            }
        })
    }
}
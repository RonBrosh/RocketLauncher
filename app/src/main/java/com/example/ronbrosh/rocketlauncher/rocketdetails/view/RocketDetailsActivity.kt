package com.example.ronbrosh.rocketlauncher.rocketdetails.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Launch
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.example.ronbrosh.rocketlauncher.rocketdetails.view.model.RocketDetailsViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.squareup.picasso.Picasso
import java.util.*


class RocketDetailsActivity : AppCompatActivity() {
    private lateinit var rocketDetailsViewModel: RocketDetailsViewModel
    private lateinit var lineChart: LineChart

    companion object {
        private const val INTENT_EXTRA_ROCKET_ID: String = "INTENT_EXTRA_ROCKET_ID"
        private const val INTENT_EXTRA_ROCKET_NAME: String = "INTENT_EXTRA_ROCKET_NAME"

        fun newIntent(context: Context, rocketId: String, rocketName: String): Intent {
            val intent = Intent(context, RocketDetailsActivity::class.java)
            intent.putExtra(RocketDetailsActivity.INTENT_EXTRA_ROCKET_ID, rocketId)
            intent.putExtra(RocketDetailsActivity.INTENT_EXTRA_ROCKET_NAME, rocketName)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_details)

        // Get the selected rocket id.
        val rocketId = intent.getStringExtra(INTENT_EXTRA_ROCKET_ID) ?: return

        // Set action bar title.
        val rocketName: String = intent.getStringExtra(INTENT_EXTRA_ROCKET_NAME)
        rocketName.let { title ->
            supportActionBar?.let { actionBar ->
                actionBar.title = title
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
            val fade = Fade()
            fade.excludeTarget(R.id.action_bar_container, true)
            fade.excludeTarget(android.R.id.statusBarBackground, true)
            fade.excludeTarget(android.R.id.navigationBarBackground, true)
            window.enterTransition = fade
            window.exitTransition = fade
        }

        // Init view model.
        rocketDetailsViewModel = RocketDetailsViewModel(application, rocketId)
        rocketDetailsViewModel.getRocketWithLaunchListLiveData().observe(this, Observer {
            initRocketDetails(it.rocket)
            if (it.launchList.isEmpty()) {
                rocketDetailsViewModel.fetchRocketLaunchList(rocketId)
            } else {
                setLineChartData(it.launchList)
            }
        })

        // Init line chart.
        initLineChart()
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

    private fun initRocketDetails(rocket: Rocket) {
        findViewById<TextView>(R.id.textViewRocketName).text = rocket.name
        findViewById<TextView>(R.id.textViewRocketCountry).text = rocket.country
        findViewById<TextView>(R.id.textViewRocketEnginesCount).text = String.format(getString(R.string.rocket_data_engines_count_format), rocket.engine.enginesCount)
        Picasso.get().load(rocket.imageUrlList[0]).into(findViewById<ImageView>(R.id.imageViewPreview))
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
        lineChart.setGridBackgroundColor(if (Build.VERSION.SDK_INT >= 23) getColor(R.color.lightGray) else resources.getColor(R.color.lightGray))
        lineChart.setNoDataText(getString(R.string.line_chart_no_data_text))
        lineChart.setNoDataTextColor(if (Build.VERSION.SDK_INT >= 23) getColor(R.color.colorPrimaryDark) else resources.getColor(R.color.colorPrimaryDark))

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
        lineDataSet.fillColor = if (Build.VERSION.SDK_INT >= 23) getColor(R.color.colorAccent75) else resources.getColor(R.color.colorAccent75)
        lineDataSet.color = if (Build.VERSION.SDK_INT >= 23) getColor(R.color.colorPrimary) else resources.getColor(R.color.colorPrimary)
        lineDataSet.valueTextColor = if (Build.VERSION.SDK_INT >= 23) getColor(R.color.colorPrimaryDark) else resources.getColor(R.color.colorPrimaryDark)
        lineDataSet.lineWidth = resources.getDimension(R.dimen.line_chart_line_width)
        lineDataSet.circleColors = List(1) { if (Build.VERSION.SDK_INT >= 23) getColor(R.color.colorPrimary) else resources.getColor(R.color.colorPrimary) }
        lineDataSet.circleRadius = resources.getDimension(R.dimen.line_chart_value_circle_radius)
        lineDataSet.circleHoleColor = if (Build.VERSION.SDK_INT >= 23) getColor(R.color.lightGray) else resources.getColor(R.color.lightGray)
        lineDataSet.circleHoleRadius = resources.getDimension(R.dimen.line_chart_value_circle_hole_radius)
        lineDataSet.valueTextSize = resources.getDimension(R.dimen.text_mini)
        lineDataSet.valueFormatter = IValueFormatter { value: Float, _: Entry, _: Int, _: ViewPortHandler ->
            return@IValueFormatter "" + value.toInt()
        }

        // Set the data to line chart.
        lineChart.data = LineData(lineDataSet)
        lineChart.invalidate()
    }
}
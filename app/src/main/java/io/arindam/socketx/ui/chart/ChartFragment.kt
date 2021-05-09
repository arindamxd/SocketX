package io.arindam.socketx.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.arindam.socketx.R
import io.arindam.socketx.data.local.Repository
import io.arindam.socketx.data.model.CityAQI
import io.arindam.socketx.ui.home.HomeViewModel
import io.arindam.socketx.util.ViewModelFactory
import io.arindam.socketx.util.getSeconds
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlin.math.roundToInt

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

class ChartFragment private constructor() : Fragment() {

    companion object {
        const val TAG = "ChartFragment"
        fun newInstance(): ChartFragment = ChartFragment()
    }

    private var viewModel: HomeViewModel? = null
    private var listLiveData: LiveData<CityAQI>? = null

    private var lastUpdatedTime: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_chart, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = initViewModel()
    }

    private fun initViewModel(): HomeViewModel {
        val model: HomeViewModel by viewModels { ViewModelFactory(Repository.getInstance(requireContext())) }
        return model
    }

    fun updateCity(cityName: String) {
        fetch(cityName = cityName)
        city_name.text = cityName

        listLiveData = viewModel?.getLatestByCityName(cityName)
        listLiveData?.observe(this, { fetch(it, cityName) })
    }

    fun removeObserver() {
        lastUpdatedTime = 0L
        chart_view.clear()
        listLiveData?.removeObservers(this)
    }

    private fun fetch(data: CityAQI? = null, cityName: String) {
        if (lastUpdatedTime == 0L) {
            viewModel?.getEarlierByCityName(cityName)?.observe(this, { dataList ->
                viewModel?.getEarlierByCityName(cityName)?.removeObservers(this@ChartFragment)
                update(getEntries(dataList))
            })
        } else data?.let { update(getEntries(listOf(it))) }
    }

    private fun update(entries: List<Entry>) {
        val lineDataSet: LineDataSet
        if (chart_view.data != null && chart_view.data.dataSetCount > 0) {
            lineDataSet = chart_view.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet.values = entries

            chart_view.data.notifyDataChanged()
            chart_view.notifyDataSetChanged()
        } else {
            lineDataSet = LineDataSet(entries, "Air Quality Index")
            lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            lineDataSet.lineWidth = 1.8f
            lineDataSet.circleRadius = 1f
            lineDataSet.setDrawFilled(true)

            val dataSets: MutableList<ILineDataSet> = mutableListOf()
            dataSets.add(lineDataSet)

            val data = LineData(dataSets)
            chart_view.data = data
            chart_view.description.isEnabled = false
        }
        chart_view.invalidate()
    }

    private fun getEntries(list: List<CityAQI>): List<Entry> {
        val entries: MutableList<Entry> = ArrayList()
        list.forEach {
            if (lastUpdatedTime == 0L) lastUpdatedTime = it.timestamp.getSeconds()
            val interval = it.timestamp.getSeconds() - lastUpdatedTime
            entries.add(Entry(interval.toFloat(), it.aqi.roundToInt().toFloat()))
        }
        return entries
    }
}

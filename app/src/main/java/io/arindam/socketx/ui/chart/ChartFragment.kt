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
import io.arindam.socketx.util.getMinute
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
    private var listLiveData: LiveData<List<CityAQI>>? = null

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
        city_name.text = cityName

        listLiveData = viewModel?.getListByCityName(cityName)
        listLiveData?.observe(this, {
            updateChart(getEntries(it), cityName)
        })
    }

    fun removeObserver() {
        listLiveData?.removeObservers(this)
    }

    private fun updateChart(entries: List<Entry>, city: String) {
        chart_view.clear()

        val lineDataSet = LineDataSet(entries, "AQI")
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.lineWidth = 1.8f
        lineDataSet.circleRadius = 1f
        lineDataSet.setDrawFilled(true)

        val dataSets: MutableList<ILineDataSet> = mutableListOf()
        dataSets.add(lineDataSet)

        val data = LineData(dataSets)
        chart_view.data = data
        chart_view.description.text = city
        chart_view.invalidate()
    }

    private fun getEntries(list: List<CityAQI>): List<Entry> {
        val entries: MutableList<Entry> = ArrayList()
        list.forEach {
            entries.add(Entry(it.timestamp.getMinute(), it.aqi.roundToInt().toFloat()))
        }
        return entries
    }
}

package io.arindam.socketx.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.arindam.socketx.R
import io.arindam.socketx.data.model.CityAQI
import io.arindam.socketx.listener.CityAQIListener

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

class CityAQIAdapter(
    private val cityList: List<CityAQI>,
    private val listener: CityAQIListener
) : RecyclerView.Adapter<CityAQIViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAQIViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_city_item, parent, false)
        return CityAQIViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: CityAQIViewHolder, position: Int) {
        holder.bind(cityList[position])
    }

    override fun getItemCount(): Int {
        return cityList.size
    }
}

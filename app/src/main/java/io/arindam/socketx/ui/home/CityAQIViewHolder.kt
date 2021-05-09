package io.arindam.socketx.ui.home

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.arindam.socketx.R
import io.arindam.socketx.data.model.CityAQI
import io.arindam.socketx.listener.CityAQIListener
import io.arindam.socketx.util.getAQI
import io.arindam.socketx.util.getColorCode
import io.arindam.socketx.util.getTime

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

class CityAQIViewHolder(
    itemView: View,
    private val listener: CityAQIListener
) : RecyclerView.ViewHolder(itemView) {

    private val cityItemLayout: LinearLayout = itemView.findViewById(R.id.city_item_layout)
    private val cityNameTextView: TextView = itemView.findViewById(R.id.city_name)
    private val currentAQITextView: TextView = itemView.findViewById(R.id.current_aqi)
    private val lastUpdatedTextView: TextView = itemView.findViewById(R.id.last_updated)

    fun bind(cityAQI: CityAQI) {
        cityItemLayout.setBackgroundColor(cityAQI.aqi.getColorCode(itemView.context))

        cityNameTextView.text = cityAQI.city
        currentAQITextView.text = cityAQI.aqi.getAQI()
        lastUpdatedTextView.text = cityAQI.timestamp.getTime()

        itemView.setOnClickListener { listener.onClicked(cityAQI.city) }
    }
}

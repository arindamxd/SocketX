package io.arindam.socketx.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.arindam.socketx.data.local.Repository
import io.arindam.socketx.data.model.CityAQI

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

class HomeViewModel(private val repository: Repository) : ViewModel() {
    fun updateData(list: List<CityAQI>) = repository.cityAQIDao().insertAll(list)
    fun getLatestList(): LiveData<List<CityAQI>> = repository.cityAQIDao().getLatestList()
    fun getEarlierByCityName(cityName: String): LiveData<List<CityAQI>> = repository.cityAQIDao().getEarlierByCityName(cityName)
    fun getLatestByCityName(cityName: String): LiveData<CityAQI> = repository.cityAQIDao().getLatestByCityName(cityName)
}

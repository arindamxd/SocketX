package io.arindam.socketx.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.arindam.socketx.data.model.CityAQI

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

@Dao
interface CityAQIDao {

    @Query("SELECT id, city, aqi, max(timestamp) as 'timestamp' FROM city_table GROUP BY city ORDER BY city ASC")
    fun getLatestList(): LiveData<List<CityAQI>>

    @Query("SELECT * FROM city_table WHERE city LIKE :cityName ORDER BY timestamp ASC")
    fun getListByCityName(cityName: String): LiveData<List<CityAQI>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(cityAQIs: List<CityAQI>): List<Long>
}

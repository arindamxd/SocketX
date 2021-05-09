package io.arindam.socketx.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

@JsonClass(generateAdapter = true)
@Entity(tableName = "city_table")
data class CityAQI(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "aqi") val aqi: Double,
    @ColumnInfo(name = "timestamp") val timestamp: Long = Calendar.getInstance().timeInMillis
)

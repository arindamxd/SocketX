package io.arindam.socketx.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.arindam.socketx.data.model.CityAQI
import io.arindam.socketx.ui.MainActivity.Companion.DATABASE_NAME

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

@Database(entities = [CityAQI::class], version = 1)
abstract class Repository : RoomDatabase() {

    companion object {

        @Volatile
        private var instance: Repository? = null

        @JvmStatic
        fun getInstance(context: Context): Repository = synchronized(this) {
            if (instance == null) instance = buildDatabase(context)
            return instance as Repository
        }

        private fun buildDatabase(context: Context): Repository = Room.databaseBuilder(
            context,
            Repository::class.java,
            DATABASE_NAME
        ).build()
    }

    abstract fun cityAQIDao(): CityAQIDao
}

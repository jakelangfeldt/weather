package com.jakelangfeldt.weather.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jakelangfeldt.weather.data.datasource.local.dao.ForecastsCacheDao
import com.jakelangfeldt.weather.data.datasource.local.entity.ForecastsCache

@Database(entities = [ForecastsCache::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun forecastsCacheDao(): ForecastsCacheDao

    companion object {
        const val DATABASE_NAME = "weather-app-database"
    }
}

package com.jakelangfeldt.weather.data.repository

import com.jakelangfeldt.weather.data.datasource.response.Coordinates
import com.jakelangfeldt.weather.data.datasource.response.Forecasts

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

interface OpenWeatherMapRepository {

    suspend fun getCoordinates(zip: Int, appId: String): Result<Coordinates>
    suspend fun getForecasts(lat: Double, lon: Double, appId: String): Result<Forecasts>
}

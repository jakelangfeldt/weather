package com.jakelangfeldt.weather.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakelangfeldt.weather.BuildConfig
import com.jakelangfeldt.weather.data.datasource.response.Coordinates
import com.jakelangfeldt.weather.data.repository.OpenWeatherMapRepository
import com.jakelangfeldt.weather.data.repository.Result
import com.jakelangfeldt.weather.domain.FormatTemperatureUseCase
import com.jakelangfeldt.weather.ui.viewmodel.state.Forecast
import com.jakelangfeldt.weather.ui.viewmodel.state.ForecastsState
import com.jakelangfeldt.weather.ui.viewmodel.state.Temperature
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.jakelangfeldt.weather.data.datasource.response.Forecast as ForecastResponse
import com.jakelangfeldt.weather.data.datasource.response.Forecasts as ForecastsResponse
import com.jakelangfeldt.weather.data.datasource.response.Temperature as TemperatureResponse

@HiltViewModel
class ForecastsViewModel @Inject constructor(
    private val formatTemperatureUseCase: FormatTemperatureUseCase,
    private val openWeatherMapRepository: OpenWeatherMapRepository,
) :
    ViewModel() {

    private val _forecastsState = MutableLiveData<ForecastsState>()
    val forecastsState: LiveData<ForecastsState>
        get() = _forecastsState

    fun fetchData(zip: Int) {
        viewModelScope.launch {
            fetchCoordinates(zip)
        }
    }

    private suspend fun fetchCoordinates(zip: Int) {
        val coordinatesResult =
            openWeatherMapRepository.getCoordinates(zip, BuildConfig.OPEN_WEATHER_MAP_API_KEY)

        if (coordinatesResult is Result.Success<Coordinates> && coordinatesResult.data.lat != null && coordinatesResult.data.lon != null) {
            fetchForecasts(coordinatesResult.data.lat, coordinatesResult.data.lon, coordinatesResult.data.name)
        } else {
            // TODO
        }
    }

    private suspend fun fetchForecasts(lat: Double, lon: Double, name: String?) {
        val forecastsResult = openWeatherMapRepository.getForecasts(
            lat,
            lon,
            BuildConfig.OPEN_WEATHER_MAP_API_KEY
        )

        if (forecastsResult is Result.Success<ForecastsResponse>) {
            _forecastsState.value = forecastsResult.data.toForecastsState(name)
        } else {
            // TODO
        }
    }

    private fun ForecastsResponse.toForecastsState(name: String?) = ForecastsState(
        name = name,
        forecasts = this.daily?.map { it.toForecast() }.orEmpty(),
    )

    private fun ForecastResponse.toForecast() = Forecast(
        temperature = this.temp?.toTemperature()
    )

    private fun TemperatureResponse.toTemperature() = Temperature(
        min = formatTemperatureUseCase(this.min),
        max = formatTemperatureUseCase(this.max),
    )
}

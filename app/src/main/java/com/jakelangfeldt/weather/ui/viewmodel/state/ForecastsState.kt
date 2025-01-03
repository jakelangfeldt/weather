package com.jakelangfeldt.weather.ui.viewmodel.state

data class ForecastsState(
    val name: String? = null,
    val forecasts: List<Forecast> = emptyList(),
)

data class Forecast(
    val temperature: Temperature? = null,
)

data class Temperature(
    val min: String? = null,
    val max: String? = null,
)

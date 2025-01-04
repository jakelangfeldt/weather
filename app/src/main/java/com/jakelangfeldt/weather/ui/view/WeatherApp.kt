package com.jakelangfeldt.weather.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jakelangfeldt.weather.ui.view.screen.ForecastDetailScreen
import com.jakelangfeldt.weather.ui.view.screen.ForecastsScreen
import com.jakelangfeldt.weather.ui.viewmodel.ForecastsViewModel
import com.jakelangfeldt.weather.ui.viewmodel.state.Forecast
import com.jakelangfeldt.weather.ui.viewmodel.state.ForecastsState

@Composable
fun Weather(
    viewModel: ForecastsViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WeatherScreen.valueOf(
        backStackEntry?.destination?.route ?: WeatherScreen.ForecastsList.name
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            WeatherBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    viewModel.updateSelectedForecast(null)
                    navController.navigateUp()
                },
            )
        },
    ) { innerPadding ->
        val forecastsState = viewModel.forecastsState.observeAsState(initial = ForecastsState())

        LaunchedEffect(forecastsState.value.selectedForecast) {
            if (forecastsState.value.selectedForecast != null) {
                navController.navigate(WeatherScreen.ForecastDetail.name)
            }
        }

        NavHost(
            navController = navController,
            startDestination = WeatherScreen.ForecastsList.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = WeatherScreen.ForecastsList.name) {
                ForecastsScreen(
                    forecastsState = forecastsState.value,
                    onSubmitZipCode = { zipCode: Int -> viewModel.fetchData(zipCode) },
                    onListItemClick = { forecast: Forecast ->
                        viewModel.updateSelectedForecast(forecast)
                    })
            }
            composable(route = WeatherScreen.ForecastDetail.name) {
                ForecastDetailScreen(forecastsState.value.selectedForecast)
            }
        }
    }
}

enum class WeatherScreen(val title: String) {
    ForecastsList(title = "Weather"),
    ForecastDetail(title = "Forecast"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherBar(
    currentScreen: WeatherScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(currentScreen.title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            }
        }
    )
}

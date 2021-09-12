package ru.voodster.composeweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import ru.voodster.composeweather.ui.theme.ComposeWeatherTheme
import ru.voodster.composeweather.weatherapi.WeatherModel

class MainActivity : ComponentActivity() {
    
    private val viewModel:WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWeatherTheme {
                Scaffold() {
                    WeatherApp(appContainer = viewModel )
                }
            }
        }
    }
}



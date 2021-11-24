package ru.voodster.composeweather.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.voodster.cochart.TimeSeriesChart
import ru.voodster.composeweather.WeatherViewModel
import ru.voodster.composeweather.ui.theme.ComposeWeatherTheme
import ru.voodster.composeweather.weatherapi.WeatherModel

@Composable
fun ChartScreen(viewModel: WeatherViewModel) {
    val tableWeather = viewModel.tableWeather.observeAsState()
    if (tableWeather.value == null) viewModel.getTableWeather()
    Fragment(
        isRefreshing = viewModel.isTableRefreshing,
        onRefresh = { viewModel.getTableWeather() },
        data = tableWeather,
        error = viewModel.errorMsg.observeAsState()
    ) { data ->
        Chart(list = data as ArrayList<*>)
    }
}

@Composable
fun Chart(list: ArrayList<*>){
    if (list[0] is WeatherModel){
        Scaffold {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(5.dp)
            ) {
                TimeSeriesChart(
                    modifier = Modifier.sizeIn(maxHeight = 300.dp),
                    xList = list.map { (it as WeatherModel).date.toLong().times(1000) }
                        .toTypedArray(),
                    yList = list.map { (it as WeatherModel).temp.toFloat().div(10) }.toTypedArray(),
                    grid = true,
                    textHeight = 50f, true
                )
            }
        }
    }

}

@Preview("Chart")
@Preview("Chart. Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ChartPreview() {
    ComposeWeatherTheme() {
        Scaffold(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                val mockX = listOf(1.0f, 8.0f, 1.0f, 16.0f, 32.0f)
                val mockY = listOf(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)
            }
        }
    }
}


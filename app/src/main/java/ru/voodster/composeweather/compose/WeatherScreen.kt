package ru.voodster.composeweather.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.voodster.composeweather.WeatherViewModel
import ru.voodster.composeweather.ui.theme.ComposeWeatherTheme
import ru.voodster.composeweather.ui.theme.secondaryColor
import ru.voodster.composeweather.ui.theme.secondaryTextColor
import ru.voodster.composeweather.weatherapi.WeatherModel



@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val currentWeather = viewModel.currentWeather.observeAsState() //State<WeatherModel?>
    if (currentWeather.value == null) viewModel.getCurrentWeather()
    Fragment(
        isRefreshing = viewModel.isWeatherRefreshing,
        onRefresh = { viewModel.getCurrentWeather() },
        data = viewModel.currentWeather.observeAsState(),
        error = viewModel.errorMsg.observeAsState()
    ) {
        CurrentWeather(data = it as WeatherModel)
    }
}


@Composable
fun CurrentWeather(data: WeatherModel) {
    LazyColumn(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        item { TextOnSurface(text = data.strFullDate(), textSize = 40.sp) }
        item { TextOnSurface(text = data.strTemp(), textSize = 60.sp) }
        item { TextOnSurface(text = data.strPress(), textSize = 40.sp) }
        item { TextOnSurface(text = data.strHum(), textSize = 40.sp) }
    }
}


@Composable
fun TextOnSurface(text: String, textSize: TextUnit) {
    Surface(
        shape = RoundedCornerShape(3.dp),
        color = secondaryColor
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = textSize,
            color = secondaryTextColor,
            text = text
        )
    }
}

@Preview("WeatherScreen")
@Preview("WeatherScreen fontsScale 1.5", fontScale = 1.5f)
@Composable
fun DefaultPreview() {
    ComposeWeatherTheme {
        CurrentWeather(data = WeatherModel().fakeWeather())
    }
}

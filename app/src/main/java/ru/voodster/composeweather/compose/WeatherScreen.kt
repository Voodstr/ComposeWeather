package ru.voodster.composeweather.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.voodster.composeweather.WeatherViewModel
import ru.voodster.composeweather.ui.theme.ComposeWeatherTheme
import ru.voodster.composeweather.ui.theme.primaryDarkColor
import ru.voodster.composeweather.ui.theme.secondaryColor
import ru.voodster.composeweather.ui.theme.secondaryTextColor
import ru.voodster.composeweather.weatherapi.WeatherModel
import java.text.SimpleDateFormat
import java.util.*


val fakeWeather = WeatherModel(1331377702, 0, 50, 0, 755, 200)
val nullWeather = WeatherModel(0, 0, 0, 0, 0, 0)

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val currentWeather = viewModel.currentWeather.observeAsState()
    val error = viewModel.errorMsg.observeAsState()
    if (currentWeather.value == null) viewModel.getCurrentWeather()
    Scaffold(
        backgroundColor = primaryDarkColor, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(viewModel.isRefreshing),
            onRefresh = { viewModel.getCurrentWeather() },
            content = {
                HomeScreenErrorAndContent(
                    data = currentWeather.value,
                    error = error.value,
                    onRefresh = { viewModel.getCurrentWeather() }
                ) { data ->
                    CurrentWeather(data = data as WeatherModel)
                }
            })
    }
}

@Composable
private fun HomeScreenErrorAndContent(
    data: Any?,
    error: String?,
    onRefresh: () -> Unit,
    content: @Composable (Any) -> Unit
) {
    when {
        data != null -> {
            content(data)
        }
        error == null -> {
            FullScreenLoading()
        }
        else -> {
            ErrorScreen(error, onRefresh = onRefresh)
        }
    }
}

@Composable
fun ErrorScreen(error: String?, onRefresh: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(5.dp, 50.dp, 5.dp, 5.dp)
    ) {
        Text(text = "Network problem", fontSize = 50.sp, textAlign = TextAlign.Center)
        Text(text = error ?: "Unknown", fontSize = 20.sp, textAlign = TextAlign.Center)
        Button(onClick = onRefresh) {
            Text(text = "REFRESH", fontSize = 50.sp)
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CurrentWeather(data: WeatherModel) {
    val fTemp = "${data.temp.toDouble().div(10.0)}°C"
    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.ROOT)
    val fDate = sdf.format(Date(data.date.toLong().times(1000)))
    val fPress = "${data.press} mm"
    val fHum = "${data.hum}%"
    LazyColumn(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        item { TextOnSurface(text = fDate, textSize = 40.sp) }
        item { TextOnSurface(text = fTemp, textSize = 60.sp) }
        item { TextOnSurface(text = fPress, textSize = 40.sp) }
        item { TextOnSurface(text = fHum, textSize = 40.sp) }
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
        CurrentWeather(data = fakeWeather)
    }
}

@Preview("Error screen")
@Composable
fun ErrorPreview() {
    ComposeWeatherTheme {
        HomeScreenErrorAndContent(data = null, error = "Unknown error", onRefresh = { /*TODO*/ }) {
        }
    }
}

@Preview("Loading")
@Composable
fun LoadingPreview() {
    ComposeWeatherTheme {
        HomeScreenErrorAndContent(data = null, error = null, onRefresh = { /*TODO*/ }) {
        }
    }
}


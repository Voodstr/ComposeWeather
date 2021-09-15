package ru.voodster.composeweather.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.voodster.composeweather.WeatherViewModel
import ru.voodster.composeweather.ui.theme.*
import ru.voodster.composeweather.weatherapi.WeatherModel
import java.util.*
import java.util.Calendar.DATE


val fakeWeather = WeatherModel(1331377702, 0, 50, 0, 755, 200)


@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val currentWeather = viewModel.currentWeather.observeAsState() //State<WeatherModel?>
    if (currentWeather.value == null) viewModel.getCurrentWeather()
    MainScreen(
        isRefreshing = viewModel.isWeatherRefreshing,
        onRefresh = { viewModel.getCurrentWeather() },
        data = viewModel.currentWeather.observeAsState(),
        error = viewModel.errorMsg.observeAsState()
    ) {
        CurrentWeather(data = it as WeatherModel)
    }
}

@Composable
fun TableScreen(viewModel: WeatherViewModel) {
    val tableWeather = viewModel.tableWeather.observeAsState()
    if (tableWeather.value == null) viewModel.getTableWeather()
    MainScreen(
        isRefreshing = viewModel.isTableRefreshing,
        onRefresh = { viewModel.getTableWeather() },
        data = tableWeather,
        error = viewModel.errorMsg.observeAsState()
    ) { data ->
        WeatherList(data = data as List<*>)
    }
}

@Composable
fun MainScreen(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    data: State<Any?>,
    error: State<String?>,
    content: @Composable (Any) -> Unit
) {
    Scaffold(
        backgroundColor = primaryDarkColor, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { onRefresh() },
            content = {
                HomeScreenErrorAndContent(
                    data = data.value,
                    error = error.value,
                    onRefresh = { onRefresh() }
                ) { data -> content(data) }
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
        modifier = Modifier.padding(5.dp, 5.dp).fillMaxWidth().fillMaxHeight()
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
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(), verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "LOADING", fontSize = 40.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator()
        }
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
fun WeatherList(data: List<*>) {
    LazyColumn {
        var preDay = Calendar.getInstance().get(DATE)
        item { NewDayText(text = "Today") }
        data.forEach {
            if (it is WeatherModel) {

                val curDate = Calendar.getInstance() // ищу начало дня и вставляю NewDayText
                curDate.time = Date(it.date.toLong().times(1000))
                val curDay = curDate.get(DATE)
                if (preDay > curDay) {
                    item {
                        NewDayText(text = it.strDayOfMonth())
                    }
                }
                preDay = curDay


                item {
                    WeatherRow(
                        data = it,
                        textColor = secondaryTextColor,
                        rowColor = secondaryColor
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherRow(data: WeatherModel, rowColor: Color, textColor: Color) {
    Surface(
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp, 2.dp),
        color = rowColor,
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp, 8.dp)
                .fillMaxWidth(), Arrangement.SpaceEvenly
        ) {
            RowText(data.strTime(), textColor)
            RowText(data.strTemp(), textColor)
            RowText(data.strPress(), textColor)
            RowText(data.strHum(), textColor)
        }
    }
}

@Composable
fun NewDayText(text: String) {
    Text(
        text = text,
        Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 30.sp,
        color = primaryTextColor
    )
}

@Composable
private fun RowText(
    text: String,
    color: Color
) {
    val s = 20.sp
    val textPadding = Modifier.padding(5.dp)
    Text(text = text, modifier = textPadding, fontSize = s, color = color)
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

@Preview("Row")
@Composable
fun RowPreview() {
    WeatherRow(data = fakeWeather, textColor = secondaryTextColor, rowColor = secondaryColor)
}

@Preview("Row Big Font", fontScale = 1.5f)
@Composable
fun RowBigFontPreview() {
    WeatherRow(data = fakeWeather, textColor = secondaryTextColor, rowColor = secondaryColor)
}


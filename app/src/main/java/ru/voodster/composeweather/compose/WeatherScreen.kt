package ru.voodster.composeweather.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.voodster.composeweather.WeatherRepository
import ru.voodster.composeweather.ui.theme.*
import ru.voodster.composeweather.weatherapi.WeatherModel
import java.text.SimpleDateFormat
import java.util.*



/**
 * Full screen circular progress indicator
 */
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



/**
 * Display an initial empty state or swipe to refresh content.
 *
 * @param empty (state) when true, display [emptyContent]
 * @param emptyContent (slot) the content to display for the empty state
 * @param loading (state) when true, display a loading spinner over [content]
 * @param onRefresh (event) event to request refresh
 * @param content (slot) the main content to show
 */
@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = onRefresh,
            content = content,
        )
    }
}



@Composable
fun emptyScreen(){
    Column(modifier = Modifier.fillMaxHeight()) {
        TextOnSurface(text = "Just started. Wait please", textSize =50.sp)
    }

}

@Composable
fun Content(data: WeatherModel){
    val fTemp = "${data.temp.toDouble().div(10.0)}°C"
    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.ROOT)
    val fDate = sdf.format(Date(data.date.toLong().times(1000)))
    val fPress = "${data.press} mm"
    val fHum = "${data.hum}%"
    Scaffold(
    ) {
        Surface(color = primaryColor) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight(),
                Arrangement.SpaceEvenly
            ) {
                TextOnSurface(text = fDate, textSize = 50.sp)
                TextOnSurface(text = fTemp, textSize = 70.sp)
                TextOnSurface(text = fPress, textSize = 50.sp)
                TextOnSurface(text = fHum, textSize = 50.sp)
            }
        }
    }
}



@Composable
fun TextOnSurface(text:String,textSize:TextUnit){
    Surface(shape = RoundedCornerShape(50.dp),
        color = secondaryColor) {
            Text(modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = textSize,
                color = secondaryTextColor,
                text = text)
        }
}

@Composable
fun WeatherScreen(weatherRepository: WeatherRepository) {
    var initialLoad = true
    var weatherError = "Unknown Error"
    var currentWeather = WeatherModel(0,0,0,0,0,0)
    var loading = false
    val load = rememberSwipeRefreshState(loading)
    
    SwipeRefresh(state = load, onRefresh = { 
        loading = true
        weatherRepository.getCurrentWeather(object : WeatherRepository.GetWeatherCallback {
            override fun onSuccess(result: WeatherModel) {
                currentWeather = result
                loading = false
            }
            override fun onError(error: String?) {
                weatherError = error ?: "Unknown error"
                loading = false
            }
        })
    }) {
        Content(data = currentWeather)
    }
}

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ComposeWeatherTheme {
            Content(data = WeatherModel(1630673409,0,60,0,755,200))
        }
    }
package ru.voodster.composeweather.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import ru.voodster.composeweather.WeatherRepository
import ru.voodster.composeweather.WeatherViewModel
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
fun WeatherScreen(data: WeatherModel?, refreshState: SwipeRefreshState, onRefresh: () -> Unit) {

        Surface(
            color = primaryDarkColor, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            SwipeRefresh(refreshState, onRefresh = onRefresh) {
                WeatherList(data = data?:WeatherModel(9,9,9,9,9,9))
            }
        }
}
@Composable
fun ContentOrError(){
    val refreshState by remember { mutableStateOf(false)}

}

@Composable
fun WeatherList(data: WeatherModel){
    val fTemp = "${data.temp.toDouble().div(10.0)}°C"
    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.ROOT)
    val fDate = sdf.format(Date(data.date.toLong().times(1000)))
    val fPress = "${data.press} mm"
    val fHum = "${data.hum}%"
    LazyColumn(verticalArrangement = Arrangement.SpaceEvenly,modifier = Modifier.fillMaxHeight()){
        item{TextOnSurface(text = fDate, textSize = 50.sp)}
        item{TextOnSurface(text = fTemp, textSize = 70.sp)}
        item{TextOnSurface(text = fPress, textSize = 50.sp)}
        item{TextOnSurface(text = fHum, textSize = 50.sp)}
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

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ComposeWeatherTheme {
            WeatherList(data = WeatherModel(0,0,0,0,0,0))
        }
    }
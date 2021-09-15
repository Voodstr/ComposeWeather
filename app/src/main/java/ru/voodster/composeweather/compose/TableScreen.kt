package ru.voodster.composeweather.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.voodster.composeweather.WeatherViewModel
import ru.voodster.composeweather.ui.theme.ComposeWeatherTheme
import ru.voodster.composeweather.weatherapi.WeatherModel
import java.util.*
import java.util.Calendar.DATE


@Composable
fun TableScreen(viewModel: WeatherViewModel) {
    val tableWeather = viewModel.tableWeather.observeAsState()
    if (tableWeather.value == null) viewModel.getTableWeather()
    Fragment(
        isRefreshing = viewModel.isTableRefreshing,
        onRefresh = { viewModel.getTableWeather() },
        data = tableWeather,
        error = viewModel.errorMsg.observeAsState()
    ) { data ->
        WeatherList(data = data as List<*>)
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
                        data = it
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherRow(data: WeatherModel) {
    Surface(
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp, 5.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.SpaceEvenly
        ) {
            RowText(data.strTime())
            RowText(data.strTemp())
            RowText(data.strPress())
            RowText(data.strHum())
        }
    }
}

@Composable
fun NewDayText(text: String) {
    Text(
        text = text,
        Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 30.sp
    )
}

@Composable
private fun RowText(
    text: String
) {
    val s = 20.sp
    val textPadding = Modifier.padding(5.dp)
    Text(text = text, modifier = textPadding, fontSize = s)
}



@Preview("Table Screen")
@Preview("Table Screen. Dark Theme",uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("Table Screen. Big font", fontScale = 1.3f)
@Composable
fun ScreenPreview() {
    ComposeWeatherTheme() {
        Scaffold {
            val list = arrayListOf(WeatherModel().fakeWeather(0))
            var offset = 0
            for (i in 1..20) {
                offset += (60..300).random()
                list.add(WeatherModel().fakeWeather(offset))
            }
            WeatherList(
                data = list
            )
        }
    }

}



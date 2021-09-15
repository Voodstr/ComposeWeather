package ru.voodster.composeweather.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.voodster.composeweather.WeatherViewModel
import ru.voodster.composeweather.ui.theme.*
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


@Preview("Row")
@Preview("Row. Big font", fontScale = 1.5f)
@Composable
fun RowPreview() {
    WeatherRow(data = WeatherModel().fakeWeather(), textColor = secondaryTextColor, rowColor = secondaryColor)
}


@Preview("Table Screen")
@Preview("Table Screen. Big font", fontScale = 1.4f)
@Preview("Table Screen. Small font", fontScale = 0.8f)
@Composable
fun ScreenPreview(){
    val list= arrayListOf(WeatherModel().fakeWeather(0))
    var offset = 0
    for (i in 1..20){
        offset+=(60..300).random()
        list.add(WeatherModel().fakeWeather(offset))
    }
    WeatherList(
        data = list
    )
}



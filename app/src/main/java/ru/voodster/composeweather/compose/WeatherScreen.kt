package ru.voodster.composeweather.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.voodster.composeweather.ui.theme.*
import ru.voodster.composeweather.weatherapi.WeatherModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
    fun Weather(data: WeatherModel) {
    Surface(color = primaryColor) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp),
            Arrangement.SpaceEvenly
        ) {
            date(date =data.date )
            temp(temp = data.temp.toDouble().div(10.0))
            pressure(pressure = data.press)
            humidity(humidity = data.hum)
        }
    }
    }

@Composable
fun temp(temp:Double){
    Surface(shape = RoundedCornerShape(50.dp),
        color = secondaryDarkColor,
        border = BorderStroke(1.dp, primaryDarkColor)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)) {
            Text(modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,fontSize = 100.sp,
                color = secondaryTextColor,
                text = "${temp}° C")
        }
    }
}

@Composable
fun date(date:Int){
    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.ROOT)
    val compiledDate = sdf.format(Date(date.toLong().times(1000)))

    Surface(shape = RoundedCornerShape(50.dp),
        color = secondaryDarkColor,
        border = BorderStroke(1.dp, primaryDarkColor)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)) {
            Text(text = compiledDate,
                textAlign = TextAlign.Center,
                fontSize = 60.sp,
                color = secondaryTextColor
            )
        }
    }
}

@Composable
fun pressure(pressure : Int){
    Surface(shape = RoundedCornerShape(50.dp)
        ,color = secondaryDarkColor,
        border = BorderStroke(1.dp, primaryDarkColor)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)) {
            Text(text = "$pressure мм.",textAlign = TextAlign.Center,fontSize = 60.sp,
                color = secondaryTextColor)
        }
    }
}

@Composable
fun humidity(humidity : Int){
    Surface(shape = RoundedCornerShape(50.dp),
        color = secondaryDarkColor,
        border = BorderStroke(1.dp, primaryDarkColor)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)) {
            Text(text = "$humidity %",textAlign = TextAlign.Center,fontSize = 60.sp,
                color = secondaryTextColor)
        }
    }
}


@Preview(showBackground = true)
@Composable
    fun DataPreviews(){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(10.dp),
        Arrangement.SpaceEvenly
    ) {
            temp(temp = 20.0)
            date(date =1630673409 )
            pressure(pressure = 755)
        }
    }



    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ComposeWeatherTheme {
            Weather(data = WeatherModel(1630673409,0,60,0,755,200))
        }
    }
package ru.voodster.composeweather.weatherapi

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class WeatherModel(

    @SerializedName("Date") var date: Int = 0,
    @SerializedName("HomeT") var homeT: Int = 0,
    @SerializedName("Hum") var hum: Int = 0,
    @SerializedName("ID") var iD: Int = 0,
    @SerializedName("Press") var press: Int = 0,
    @SerializedName("Temp") var temp: Int = 0
) {
    /**
     * Str Temp
     *
     *  Возвращает строку температуры
     *  в цельсиях
     */
    fun strTemp() =
        "${temp.toDouble().div(10.0)}°C"

    /**
     * Возращает строку влажности
     *
     */
    fun strHum() = "${hum}%"

    /**
     * Str press
     *
     */

    fun strPress() = "$press mm"

    /**
     * Str date
     *
     */
    fun strFullDate(): String =
        SimpleDateFormat("dd/MM HH:mm", Locale.ROOT)
            .format(Date(date.toLong().times(1000)))

    fun strTime(): String =
        SimpleDateFormat("HH:mm", Locale.ROOT)
            .format(Date(date.toLong().times(1000)))

    fun strDayOfMonth(): String =
        SimpleDateFormat("dd/MM", Locale.ROOT)
            .format(Date(date.toLong().times(1000)))

    /**
     * Fake weather
     *
     * @param dateOffset - in minutes
     * @return
     */
    fun fakeWeather(dateOffset:Int):WeatherModel=
        WeatherModel(Calendar.getInstance().timeInMillis.minus(dateOffset.times(60000)).div(1000).toInt(),
            (100..250).random(),
            (10..95).random(),
            0,
            (730..785).random(),
            (-200..300).random())

    fun fakeWeather():WeatherModel=
        WeatherModel(Calendar.getInstance().timeInMillis.div(1000).toInt(),
            (100..250).random(),
            (10..95).random(),
            0,
            (730..785).random(),
            (-200..300).random())

}
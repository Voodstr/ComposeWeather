package ru.voodster.composeweather

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.voodster.composeweather.db.WeatherDatabase
import ru.voodster.composeweather.weatherapi.WeatherModel
import ru.voodster.composeweather.weatherapi.WeatherService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val db: WeatherDatabase,
    private val api: WeatherService
) {


    private val fakeWeather = WeatherModel(0, 0, 0, 0, 0, 0)
    private var curWeather: WeatherModel = fakeWeather

    private val tableWeather = ArrayList<WeatherModel>()

    fun getCurrentWeather(callback: GetWeatherCallback) {
        api.getWeather()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                run {
                    curWeather = result
                    callback.onSuccess(curWeather)
                }
            }, { error ->
                callback.onError(error.localizedMessage)
            })
    }

    fun getTableWeather(callback: GetTableWeatherCallback) {
        api.getTableData(300)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ r ->
                run {
                    tableWeather.clear()
                    tableWeather.addAll(r)
                    callback.onSuccess(tableWeather)
                }
            }, { error ->
                callback.onError(error.localizedMessage)
            })
    }

    interface GetWeatherCallback {
        fun onSuccess(result: WeatherModel)
        fun onError(error: String?)
    }

    interface GetTableWeatherCallback {
        fun onSuccess(result: List<WeatherModel>)
        fun onError(error: String?)
    }
}
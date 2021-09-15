package ru.voodster.composeweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.voodster.composeweather.weatherapi.WeatherModel


class WeatherViewModel : ViewModel() {
    private val viewModelComponent = DaggerWeatherRepoComponent.builder().build()
    private val weatherRepository = viewModelComponent.repos()

    init {
        Log.d("Weather viewModel", this.toString())
    }

    private val currentWeatherLiveData = MutableLiveData<WeatherModel>()
    val currentWeather: LiveData<WeatherModel>
        get() = currentWeatherLiveData
    var isWeatherRefreshing = false

    private val tableWeatherLiveData = MutableLiveData<List<WeatherModel>>()
    val tableWeather: LiveData<List<WeatherModel>>
        get() = tableWeatherLiveData
    var isTableRefreshing = false

    val errorMsg = SingleLiveEvent<String>()


    fun getCurrentWeather() {
        isWeatherRefreshing = true
        weatherRepository.getCurrentWeather(object : WeatherRepository.GetWeatherCallback {
            override fun onError(error: String?) {
                errorMsg.postValue(error ?: "Unknown Error")
                isWeatherRefreshing = false
            }
            override fun onSuccess(result: WeatherModel) {
                currentWeatherLiveData.postValue(result)
                isWeatherRefreshing = false
            }
        })
    }


    fun getTableWeather() {
        isTableRefreshing = true
        weatherRepository.getTableWeather(object : WeatherRepository.GetTableWeatherCallback {
            override fun onSuccess(result: List<WeatherModel>) {
                tableWeatherLiveData.postValue(result)
                isTableRefreshing = false
            }
            override fun onError(error: String?) {
                errorMsg.postValue(error ?: "Unknown Error")
                isTableRefreshing = false
            }
        })
    }


}


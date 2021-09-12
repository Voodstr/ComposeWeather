package ru.voodster.composeweather

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.Component
import ru.voodster.composeweather.db.DbModule
import ru.voodster.composeweather.weatherapi.ApiModule
import ru.voodster.composeweather.weatherapi.WeatherModel
import javax.inject.Singleton



class WeatherViewModel : ViewModel() {
    private val viewModelComponent=DaggerWeatherRepoComponent.builder().build()
    private val weatherRepository = viewModelComponent.repos()

    init {
        Log.d("Weather viewModel", this.toString())
    }

    private val currentWeatherLiveData = MutableLiveData<WeatherModel>()
    val currentWeather : LiveData<WeatherModel>
        get() = currentWeatherLiveData

    var wetherNow = mutableStateOf(WeatherModel(0,0,0,0,0,0))
        private set

    var isRefreshing = false
    var isTableRefreshing = false

    private val tableWeatherLiveData = MutableLiveData<List<WeatherModel>>()
    val tableWeather : LiveData<List<WeatherModel>>
        get() = tableWeatherLiveData

    val errorMsg = SingleLiveEvent<String>()

    @Composable
    fun errorState() = errorMsg.observeAsState()

    fun getCurrentWeather(){
        isRefreshing = true
        weatherRepository.getCurrentWeather(object: WeatherRepository.GetWeatherCallback{
            override fun onError(error: String?) {
                errorMsg.postValue(error?:"Unknown Error")
                isRefreshing = false
            }
            override fun onSuccess(result: WeatherModel) {
                currentWeatherLiveData.postValue(result)
                errorMsg.postValue(null)
                isRefreshing = false
            }
        })
    }


    fun getTableWeather(){
        isTableRefreshing = true
        weatherRepository.getTableWeather(object: WeatherRepository.GetTableWeatherCallback{
            override fun onSuccess(result: List<WeatherModel>) {
                tableWeatherLiveData.postValue(result)
                isTableRefreshing = false
            }

            override fun onError(error: String?) {
                errorMsg.postValue(error?:"Unknown Error")
                isTableRefreshing = false
            }

        })
    }


}


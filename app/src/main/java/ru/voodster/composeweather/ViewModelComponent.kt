package ru.voodster.composeweather

import dagger.Component
import ru.voodster.composeweather.db.DbModule
import ru.voodster.composeweather.weatherapi.ApiModule
import javax.inject.Singleton

@Component(modules = [ApiModule::class, DbModule::class])
@Singleton
interface WeatherRepoComponent {

    fun repos(): WeatherRepository

}

package ru.voodster.composeweather.db

import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.voodster.composeweather.App
import javax.inject.Singleton

@Module
class DbModule {


    companion object{
        const val DATABASE_NAME = "weatherDb.db"
    }

    @Singleton
    @Provides
    fun provideDatabase(): WeatherDatabase {
        return Room.databaseBuilder(
            App.instance!!.applicationContext,
            WeatherDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }


}
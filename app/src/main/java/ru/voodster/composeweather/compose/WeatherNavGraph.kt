package ru.voodster.composeweather.compose

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.voodster.composeweather.App
import ru.voodster.composeweather.WeatherRepository
import ru.voodster.composeweather.weatherapi.WeatherModel

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val INTERESTS_ROUTE = "interests"
    const val ARTICLE_ROUTE = "post"
    const val ARTICLE_ID_KEY = "postId"
}


@Composable
fun WeatherNavGraph(
    appContainer: WeatherRepository,  // DI интерфейс, который должен выдавать обьект-репозиторий
    navController: NavHostController = rememberNavController(), // контроллер навигации
    scaffoldState: ScaffoldState = rememberScaffoldState(), // состояние экрана
    startDestination: String = MainDestinations.HOME_ROUTE // начальная точка UI
) {
    val actions = remember(navController) { MainActions(navController) } // TODO ?????
    val coroutineScope = rememberCoroutineScope() // область процесса
    val openDrawer: () -> Unit = { coroutineScope.launch { scaffoldState.drawerState.open() } }

    var currentWeather = WeatherModel(1630673409,0,60,0,755,200)


    appContainer.getCurrentWeather(object: WeatherRepository.GetWeatherCallback{
        override fun onError(error: String?) {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(error?:"Unknown error",duration = SnackbarDuration.Long)
            }
        }
        override fun onSuccess(result: WeatherModel) {
            currentWeather = result
        }
    })

    NavHost(                   // navHost -- сама вьюшка, в которой меняются окна и есть все тулбары
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.HOME_ROUTE) { // что выдавать в при переходе на домашнюю страницу
            Weather(data =  currentWeather)
        }// таких штук можно добавить сколько угодно (не забуду добавить им названия 'MainDestinations')
    }
}


/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
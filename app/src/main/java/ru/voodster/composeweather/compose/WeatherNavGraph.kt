package ru.voodster.composeweather.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.voodster.composeweather.WeatherRepository
import ru.voodster.composeweather.weatherapi.WeatherModel

object MainDestinations {
    const val HOME_ROUTE = "home/current"
    const val TABLE_ROUTE = "home/table"
    const val CHART_ROUTE = "home/chart"
}


@Composable
fun WeatherNavGraph(innerPadding:PaddingValues,
                    appContainer: WeatherRepository,  // DI интерфейс, который должен выдавать обьект-репозиторий
                    navController: NavHostController = rememberNavController(), // контроллер навигации
                    scaffoldState: ScaffoldState = rememberScaffoldState(), // состояние экрана
                    startDestination: String = MainDestinations.HOME_ROUTE // начальная точка UI
) {
    val coroutineScope = rememberCoroutineScope() // область процесса

    NavHost(                   // navHost -- сама вьюшка, в которой меняются окна и есть все тулбары
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding) // отступ для bottomNavigationBar
    ) {
        composable(MainDestinations.HOME_ROUTE) { // что выдавать в при переходе на домашнюю страницу
            WeatherScreen(weatherRepository = appContainer)
        }// таких штук можно добавить сколько угодно (не забуду добавить им названия 'MainDestinations')
        composable(MainDestinations.TABLE_ROUTE) { // что выдавать в при переходе на домашнюю страницу
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()) {
                Text(text = "TABLE",fontSize = 70.sp)
            }
        }
        composable(MainDestinations.CHART_ROUTE) { // что выдавать в при переходе на домашнюю страницу
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()) {
                Text(text = "Chart",fontSize = 70.sp)
            }
        }
    }
}


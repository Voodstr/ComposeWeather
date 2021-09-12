package ru.voodster.composeweather.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.voodster.composeweather.WeatherViewModel

object MainDestinations {
    const val HOME_ROUTE = "home/current"
    const val TABLE_ROUTE = "home/table"
    const val CHART_ROUTE = "home/chart"
}


@Composable
fun WeatherNavGraph(innerPadding:PaddingValues,
                    viewmodel: WeatherViewModel,
                    navController: NavHostController = rememberNavController(), // контроллер навигации // состояние экрана
                    startDestination: String = MainDestinations.HOME_ROUTE // начальная точка UI
) {

    NavHost(                   // navHost -- сама вьюшка, в которой меняются окна и есть все тулбары
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding) // отступ для bottomNavigationBar
    ) {
        composable(MainDestinations.HOME_ROUTE) { // что выдавать в при переходе на домашнюю страницу
            WeatherScreen(viewModel = viewmodel)
        }// таких штук можно добавить сколько угодно (не забуду добавить им названия 'MainDestinations')
        composable(MainDestinations.TABLE_ROUTE) { // что выдавать в при переходе на домашнюю страницу
            TableScreen(viewModel = viewmodel)
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


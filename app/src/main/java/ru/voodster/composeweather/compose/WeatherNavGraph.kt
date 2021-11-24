package ru.voodster.composeweather.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.voodster.composeweather.WeatherViewModel



@Composable
fun WeatherNavGraph(
    innerPadding: PaddingValues,
    viewModel: WeatherViewModel,
    navController: NavHostController = rememberNavController(), // контроллер навигации // состояние экрана
    startDestination: String = BottomNavigationScreens.CURRENT.route // начальная точка UI
) {

    NavHost(                   // navHost -- сама вьюшка, в которой меняются окна и есть все тулбары
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding) // отступ для bottomNavigationBar
    ) {
        composable(BottomNavigationScreens.CURRENT.route ) { // что выдавать в при переходе на домашнюю страницу
            WeatherScreen(viewModel = viewModel)
        }// таких штук можно добавить сколько угодно (не забуду добавить им названия 'MainDestinations')
        composable(BottomNavigationScreens.TABLE.route ) { // что выдавать в при переходе на домашнюю страницу
            TableScreen(viewModel = viewModel)
        }
        composable(BottomNavigationScreens.CHART.route ) { // что выдавать в при переходе на домашнюю страницу
            ChartScreen(viewModel = viewModel)
        }
    }
}


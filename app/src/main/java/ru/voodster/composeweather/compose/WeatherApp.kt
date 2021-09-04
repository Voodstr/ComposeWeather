package ru.voodster.composeweather

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import ru.voodster.composeweather.ui.theme.ComposeWeatherTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import ru.voodster.composeweather.compose.AppDrawer
import ru.voodster.composeweather.compose.MainDestinations
import ru.voodster.composeweather.compose.WeatherNavGraph

@Composable
fun WeatherApp(
    appContainer: WeatherRepository
) {
    ComposeWeatherTheme() { //
        ProvideWindowInsets { // обьявляем обработку вставок типа клавиатуры или navBar
            val systemUiController = rememberSystemUiController() // Контроллер системного UI -
                                                        // теже клавиатура navBar statusBar
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
            }

            val navController = rememberNavController() // Контроллер навигации
            val coroutineScope = rememberCoroutineScope() // Область процесса в котором живет UI
            // This top level scaffold contains the app drawer, which needs to be accessible
            // from multiple screens. An event to open the drawer is passed down to each
            // screen that needs it.
            val scaffoldState = rememberScaffoldState() // Состояние основного окна

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            // сохраняем последний экран, который покажется если нажать назад

            val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE
            // текущий путь, если его нет, то домашний экран

            Scaffold(
                scaffoldState = scaffoldState,
                drawerContent = {
                    AppDrawer(
                        currentRoute = currentRoute,
                        navigateToHome = { navController.navigate(MainDestinations.HOME_ROUTE) },
                        navigateToInterests = { navController.navigate(MainDestinations.INTERESTS_ROUTE) },
                        closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } }
                    )
                }
            )

            {
                WeatherNavGraph(
                    appContainer = appContainer,
                    navController = navController,
                    scaffoldState = scaffoldState
                )
            }
        }
    }
}
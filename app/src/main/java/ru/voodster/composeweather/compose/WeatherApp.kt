package ru.voodster.composeweather

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import ru.voodster.composeweather.ui.theme.ComposeWeatherTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.voodster.composeweather.compose.MainDestinations
import ru.voodster.composeweather.compose.WeatherNavGraph

const val KEY_ROUTE = "android-support-nav:controller:route"

@Composable
fun WeatherApp(
    appContainer: WeatherRepository?
) {
    ComposeWeatherTheme() { //
        ProvideWindowInsets(windowInsetsAnimationsEnabled = true) { // обьявляем обработку вставок типа клавиатуры или navBar
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

            val bottomNavigationItems = listOf(BottomNavigationScreens.Home,BottomNavigationScreens.Table,BottomNavigationScreens.Charts)

            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {bottomNavigationBar(navController, bottomNavigationItems)}
            )

            {
                WeatherNavGraph(
                    appContainer = appContainer,
                    navController = navController,
                    scaffoldState = scaffoldState,
                )
            }
        }
    }

}

@Composable
fun bottomNavigationBar(navController: NavHostController, items: List<BottomNavigationScreens>){
    BottomNavigation(){
        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon,"contentDescription") },
                label = { Text(stringResource(id = screen.resourceId)) },
                selected = currentRoute == screen.route,
                alwaysShowLabel = false, // This hides the title for the unselected items
                onClick = {
                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route)
                    }
                }
            )
        }
    }
}


sealed class BottomNavigationScreens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector ) {
    object Home : BottomNavigationScreens("Home", R.string.Indication, Icons.Filled.Home)
    object Table : BottomNavigationScreens("Table", R.string.Table, Icons.Filled.List)
    object Charts : BottomNavigationScreens("Chart", R.string.Chart, Icons.Filled.DateRange)
}


@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}


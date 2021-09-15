package ru.voodster.composeweather

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.voodster.composeweather.compose.MainDestinations

import ru.voodster.composeweather.compose.WeatherNavGraph
import ru.voodster.composeweather.ui.theme.*



private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)


private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}


@Composable
fun WeatherApp(
    appContainer: WeatherViewModel
) {
    ComposeWeatherTheme() { //
        ProvideWindowInsets(windowInsetsAnimationsEnabled = true) { // обьявляем обработку вставок типа клавиатуры или navBar
            val systemUiController = rememberSystemUiController() // Контроллер системного UI -
                                                        // теже клавиатура navBar statusBar
            SideEffect {
                systemUiController.isStatusBarVisible = false
                //systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
            }
            val navController = rememberNavController() // Контроллер навигации
            val coroutineScope = rememberCoroutineScope() // Область процесса в котором живет UI

            // This top level scaffold contains the app drawer, which needs to be accessible
            // from multiple screens. An event to open the drawer is passed down to each
            // screen that needs it.
            val scaffoldState = rememberScaffoldState() // Состояние основного окна
            val bottomNavigationItems = listOf(
                BottomNavigationScreens.CURRENT,
                BottomNavigationScreens.TABLE,
                BottomNavigationScreens.CHART)

            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {BottomNavigationBar(navController, bottomNavigationItems)}
            )
            {innerPadding->
                WeatherNavGraph(
                    viewModel = appContainer,
                    navController = navController,
                    innerPadding = innerPadding
                )
            }
        }
    }

}

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<BottomNavigationScreens>){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val sections = remember { BottomNavigationScreens.values() }
    val routes = remember { sections.map { it.route } }

    if (currentRoute in routes) {
        val currentSection = sections.first { it.route == currentRoute }
        BottomAppBar(backgroundColor = primaryLightColor,
            contentColor = primaryTextColor,
            elevation = 4.dp
        ) {
            items.forEach{section ->
                val selected = section == currentSection
                val tint by animateColorAsState(
                    if (selected) {
                        primaryTextColor
                    } else {
                        primaryDarkColor
                    }
                )
                BottomNavigationItem(
                    icon = { Icon(section.icon,"contentDescription") },
                    label = { Text(stringResource(id = section.resourceId)) },
                    selected = selected,
                    selectedContentColor = primaryTextColor ,
                    unselectedContentColor = primaryDarkColor,
                    alwaysShowLabel = true, // This hides the title for the unselected items
                    onClick = {
                        // This if check gives us a "singleTop" behavior where we do not create a
                        // second instance of the composable if we are already on that destination
                        if (currentRoute != section.route) {
                            navController.navigate(section.route){
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(findStartDestination(navController.graph).id) {
                                    saveState = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }

}



enum class BottomNavigationScreens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector ) {
    CURRENT(MainDestinations.HOME_ROUTE, R.string.Indication, Icons.Filled.Home),
    TABLE(MainDestinations.TABLE_ROUTE, R.string.Table, Icons.Filled.List),
    CHART(MainDestinations.CHART_ROUTE, R.string.Chart, Icons.Filled.DateRange)
}




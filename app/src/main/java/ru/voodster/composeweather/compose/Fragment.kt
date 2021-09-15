package ru.voodster.composeweather.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.voodster.composeweather.ui.theme.ComposeWeatherTheme
import ru.voodster.composeweather.ui.theme.primaryTextColor


@Composable
fun Fragment(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    data: State<Any?>,
    error: State<String?>,
    content: @Composable (Any) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { onRefresh() },
            content = {
                HomeScreenErrorAndContent(
                    data = data.value,
                    error = error.value,
                    onRefresh = { onRefresh() }
                ) { data -> content(data) }
            })
    }
}


@Composable
private fun HomeScreenErrorAndContent(
    data: Any?,
    error: String?,
    onRefresh: () -> Unit,
    content: @Composable (Any) -> Unit
) {
    when {
        data != null -> {
            content(data)
        }
        error == null -> {
            FullScreenLoading()
        }
        else -> {
            ErrorScreen(error, onRefresh = onRefresh)
        }
    }
}

@Composable
fun ErrorScreen(error: String?, onRefresh: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(5.dp, 5.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(text = "Network problem", fontSize = 50.sp, textAlign = TextAlign.Center)
        Text(text = error ?: "Unknown", fontSize = 20.sp, textAlign = TextAlign.Center)
        Button(onClick = onRefresh) {
            Text(text = "Refresh", fontSize = 50.sp, color = primaryTextColor)
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(), verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "LOADING", fontSize = 40.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator()
        }
    }


}


@Preview("Error screen")
@Composable
fun ErrorPreview() {
    ComposeWeatherTheme {
        HomeScreenErrorAndContent(data = null, error = "Unknown error", onRefresh = { }) {
        }
    }
}

@Preview("Loading")
@Composable
fun LoadingPreview() {
    ComposeWeatherTheme {
        HomeScreenErrorAndContent(data = null, error = null, onRefresh = { }) {
        }
    }
}

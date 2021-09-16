package ru.voodster.composeweather.compose

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.voodster.composeweather.ui.theme.ComposeWeatherTheme

@Composable
fun PolygonChart(
    xList: List<Float>, yList: List<Float>,
    modifier: Modifier,
    labelFontSize: TextUnit
) {
    val cornerOffset = 10.dp
    val surfaceColor = MaterialTheme.colors.surface
    Surface(
        modifier = modifier.clip(RoundedCornerShape(cornerOffset)),
        color = surfaceColor,
        elevation = 8.dp
    ) {
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(5.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1.0f, fill = true), horizontalArrangement = Arrangement.Start
            ) {
                Start(
                    Modifier
                        .weight(0.1f, fill = true)
                        .fillMaxHeight(),
                    yList,
                    labelFontSize
                )
                Drawing(xList = xList, yList = yList, modifier.weight(1.0f, fill = true))
            }
            Bottom(
                Modifier
                    .fillMaxWidth()
                    .weight(0.1f, fill = true),
                xList,
                labelFontSize
            )
        }

    }
}

@Composable
fun Bottom(modifier: Modifier, xList: List<Float>, fontSize: TextUnit) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = xList.minOrNull().toString(), fontSize = fontSize)
        Text(text = xList.maxOrNull().toString(), fontSize = fontSize)
    }
}

@Composable
fun Start(modifier: Modifier, yList: List<Float>, fontSize: TextUnit) {
    Column(modifier, verticalArrangement = Arrangement.SpaceBetween) {
        Text(text = yList.maxOrNull().toString(), fontSize = fontSize)
        Text(text = yList.minOrNull().toString(), fontSize = fontSize)
    }
}


@Composable
fun Drawing(xList: List<Float>, yList: List<Float>, modifier: Modifier) {
    val dotColor = MaterialTheme.colors.primary
    Box(
        modifier.padding(10.dp),
        contentAlignment = Alignment.Center,
        true
    ) {
        val lineColor = MaterialTheme.colors.onSurface
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val points = points(xList, yList, size)
            if (points != null) {
                drawPoints(
                    points = points,
                    pointMode = PointMode.Polygon,
                    lineColor,
                    strokeWidth = 5.0f
                )
                points.forEach {
                    drawCircle(
                        Brush.linearGradient(
                            colors = listOf(dotColor, dotColor)
                        ),
                        radius = 2.0f,
                        center = it,
                        style = Stroke(width = size.width * 0.025f)
                    )
                }
            }
        }
    }
}


fun points(
    xList: List<Float>,
    yList: List<Float>,
    rectSize: Size
): List<Offset>? {
    return if (xList.size == yList.size) {
        val xMax = xList.maxOrNull() ?: 100.0f
        val yMax = yList.maxOrNull() ?: 100.0f
        val ySize = yMax - (yList.minOrNull() ?: -100.0f)
        val xSize = xMax - (xList.minOrNull() ?: -100.0f)
        val xScale = rectSize.width.div(xSize)
        val yScale = rectSize.height.div(ySize)
        MutableList(xList.size) {
            Offset(
                x = xMax.minus(xList[it]).times(xScale),
                y = yMax.minus(yList.reversed()[it]).times(yScale)
            )
        }
    } else null
}


@Preview("Chart")
@Preview("Chart. Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ChartPreview() {
    ComposeWeatherTheme() {
        Scaffold(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                val mockX = listOf(1.0f, 8.0f, 1.0f, 16.0f, 32.0f)
                val mockY = listOf(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)
                PolygonChart(
                    xList = mockY, yList = mockX,
                    modifier = Modifier.size(300.dp),
                    labelFontSize = 10.sp
                )
            }
        }
    }
}


package com.example.dbmonitor.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset

@Composable
fun LiveLineChart(
    history: List<Double>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    val textPaint = remember {
        Paint().apply {
            color = android.graphics.Color.GRAY
            textSize = 32f
            textAlign = Paint.Align.LEFT
            isAntiAlias = true
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val maxDb = 120f

        val gridLevels = listOf(40f, 80f, 100f)
        gridLevels.forEach { level ->
            val y = height - (level / maxDb * height)

            drawLine(
                color = Color.Gray.copy(alpha = 0.2f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )

            drawContext.canvas.nativeCanvas.drawText(
                "${level.toInt()} dB",
                10f, 
                y - 10f, 
                textPaint
            )
        }

        if (history.size > 1) {
            val constraints = history.takeLast(50)
            val distancePerPoint = width / (50 - 1)
            
            val path = Path().apply {
                constraints.forEachIndexed { index, db ->
                    val x = index * distancePerPoint
                    val y = height - (db.toFloat().coerceIn(0f, maxDb) / maxDb * height)
                    
                    if (index == 0) moveTo(x, y) else lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

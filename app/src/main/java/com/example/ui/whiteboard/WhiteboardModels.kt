package com.example.ui.whiteboard

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

enum class WhiteboardTool {
    PEN,
    LINE,
    RECTANGLE,
    CIRCLE,
    TRIANGLE,
    FORMULA,
    ERASER
}

sealed class BoardElement {
    data class Stroke(
        val points: List<Offset>,
        val color: Color,
        val strokeWidth: Float = 4f
    ) : BoardElement()

    data class Line(
        val start: Offset,
        val end: Offset,
        val color: Color,
        val strokeWidth: Float = 4f
    ) : BoardElement()

    data class Rectangle(
        val topLeft: Offset,
        val size: Size,
        val isSquare: Boolean = false,
        val color: Color,
        val strokeWidth: Float = 4f
    ) : BoardElement()

    data class Circle(
        val center: Offset,
        val radius: Float,
        val color: Color,
        val strokeWidth: Float = 4f
    ) : BoardElement()

    data class Triangle(
        val p1: Offset,
        val p2: Offset,
        val p3: Offset,
        val color: Color,
        val strokeWidth: Float = 4f
    ) : BoardElement()

    data class Formula(
        val id: String,
        val text: String,
        val position: Offset,
        val color: Color
    ) : BoardElement()
}

object ShapeRecognizer {
    private fun distance(a: Offset, b: Offset): Float =
        hypot(a.x - b.x, a.y - b.y)

    private fun pathLength(points: List<Offset>): Float {
        var length = 0f
        for (i in 1 until points.size) {
            length += distance(points[i - 1], points[i])
        }
        return length
    }

    private fun centroid(points: List<Offset>): Offset {
        var sumX = 0f
        var sumY = 0f
        for (p in points) {
            sumX += p.x
            sumY += p.y
        }
        return Offset(sumX / points.size, sumY / points.size)
    }

    private fun boundingBox(points: List<Offset>): Pair<Offset, Size> {
        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE
        for (p in points) {
            minX = min(minX, p.x)
            minY = min(minY, p.y)
            maxX = max(maxX, p.x)
            maxY = max(maxY, p.y)
        }
        return Pair(Offset(minX, minY), Size(max(1f, maxX - minX), max(1f, maxY - minY)))
    }

    fun recognize(points: List<Offset>, color: Color): Pair<BoardElement, String>? {
        if (points.size < 6) return null

        val start = points.first()
        val end = points.last()
        val totalLen = pathLength(points)
        val directDist = distance(start, end)
        val (topLeft, size) = boundingBox(points)
        val diag = hypot(size.width, size.height)

        if (diag < 30f) return null

        val isClosed = directDist < diag * 0.35f && totalLen > diag * 1.3f

        // Check if straight line
        if (!isClosed && (directDist / totalLen) > 0.82f) {
            // Snap angle to 0, 45, 90, 135, 180
            val angle = Math.toDegrees(atan2((end.y - start.y).toDouble(), (end.x - start.x).toDouble())).toFloat()
            val snapAngles = listOf(0f, 45f, 90f, 135f, 180f, -45f, -90f, -135f, -180f)
            var finalEnd = end
            for (snap in snapAngles) {
                if (kotlin.math.abs(angle - snap) < 12f) {
                    val rad = Math.toRadians(snap.toDouble()).toFloat()
                    finalEnd = Offset(
                        start.x + cos(rad) * directDist,
                        start.y + sin(rad) * directDist
                    )
                    break
                }
            }
            return Pair(BoardElement.Line(start, finalEnd, color), "Түзу сызық ╱")
        }

        if (isClosed) {
            val center = centroid(points)
            val radii = points.map { distance(it, center) }
            val avgRadius = radii.average().toFloat()
            val radiusVariance = radii.map { kotlin.math.abs(it - avgRadius) }.average().toFloat()

            // Circle check
            if (radiusVariance / avgRadius < 0.22f) {
                return Pair(BoardElement.Circle(center, avgRadius, color), "Шеңбер ◯")
            }

            // Rectangle / Square check
            val ratio = size.width / size.height
            val isSquare = ratio in 0.78f..1.28f
            if (ratio in 0.35f..3.0f) {
                val rectSize = if (isSquare) {
                    val s = (size.width + size.height) / 2f
                    Size(s, s)
                } else size
                val name = if (isSquare) "Шаршы ⏹" else "Тіктөртбұрыш ▭"
                return Pair(BoardElement.Rectangle(topLeft, rectSize, isSquare, color), name)
            }
        }

        return null
    }
}

package com.example

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.ui.whiteboard.BoardElement
import com.example.ui.whiteboard.ShapeRecognizer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ShapeRecognizerTest {

    @Test
    fun testRecognizeStraightLine() {
        val points = listOf(
            Offset(10f, 10f),
            Offset(20f, 10f),
            Offset(30f, 10f),
            Offset(40f, 10f),
            Offset(50f, 10f),
            Offset(60f, 10f),
            Offset(70f, 10f),
            Offset(80f, 10f)
        )
        val result = ShapeRecognizer.recognize(points, Color.Black)
        assertNotNull("Should recognize straight line", result)
        assertTrue(result?.first is BoardElement.Line)
    }

    @Test
    fun testRecognizeCircle() {
        val radius = 50f
        val center = Offset(100f, 100f)
        val points = mutableListOf<Offset>()
        for (i in 0..16) {
            val angle = (i / 16.0) * 2 * Math.PI
            points.add(
                Offset(
                    (center.x + radius * Math.cos(angle)).toFloat(),
                    (center.y + radius * Math.sin(angle)).toFloat()
                )
            )
        }
        val result = ShapeRecognizer.recognize(points, Color.Blue)
        assertNotNull("Should recognize circle", result)
        assertTrue(result?.first is BoardElement.Circle)
    }
}

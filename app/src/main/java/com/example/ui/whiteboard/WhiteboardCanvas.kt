package com.example.ui.whiteboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun WhiteboardCanvas(
    modifier: Modifier = Modifier,
    elements: List<BoardElement>,
    onElementAdded: (BoardElement) -> Unit,
    onElementDeleted: (BoardElement) -> Unit,
    onFormulaMoved: (String, Offset) -> Unit,
    currentTool: WhiteboardTool,
    currentColor: Color,
    strokeWidth: Float,
    isAutoFixEnabled: Boolean,
    showGrid: Boolean
) {
    var currentPoints = remember { mutableStateListOf<Offset>() }
    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var currentDrag by remember { mutableStateOf<Offset?>(null) }
    var recognitionToast by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(recognitionToast) {
        if (recognitionToast != null) {
            delay(1800)
            recognitionToast = null
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E6F5), RoundedCornerShape(16.dp))
    ) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(currentTool, currentColor, strokeWidth, isAutoFixEnabled) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStart = offset
                            currentDrag = offset
                            if (currentTool == WhiteboardTool.PEN) {
                                currentPoints.clear()
                                currentPoints.add(offset)
                            } else if (currentTool == WhiteboardTool.ERASER) {
                                eraseNear(offset, elements, onElementDeleted)
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val newPos = (currentDrag ?: change.position) + dragAmount
                            currentDrag = newPos

                            if (currentTool == WhiteboardTool.PEN) {
                                currentPoints.add(newPos)
                            } else if (currentTool == WhiteboardTool.ERASER) {
                                eraseNear(newPos, elements, onElementDeleted)
                            }
                        },
                        onDragEnd = {
                            val start = dragStart
                            val end = currentDrag

                            if (start != null && end != null) {
                                when (currentTool) {
                                    WhiteboardTool.PEN -> {
                                        if (currentPoints.size >= 2) {
                                            if (isAutoFixEnabled) {
                                                val recognized = ShapeRecognizer.recognize(currentPoints, currentColor)
                                                if (recognized != null) {
                                                    onElementAdded(recognized.first)
                                                    recognitionToast = recognized.second
                                                } else {
                                                    onElementAdded(
                                                        BoardElement.Stroke(
                                                            points = currentPoints.toList(),
                                                            color = currentColor,
                                                            strokeWidth = strokeWidth
                                                        )
                                                    )
                                                }
                                            } else {
                                                onElementAdded(
                                                    BoardElement.Stroke(
                                                        points = currentPoints.toList(),
                                                        color = currentColor,
                                                        strokeWidth = strokeWidth
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    WhiteboardTool.LINE -> {
                                        onElementAdded(
                                            BoardElement.Line(
                                                start = start,
                                                end = end,
                                                color = currentColor,
                                                strokeWidth = strokeWidth
                                            )
                                        )
                                    }
                                    WhiteboardTool.RECTANGLE -> {
                                        val topLeft = Offset(min(start.x, end.x), min(start.y, end.y))
                                        val size = Size(kotlin.math.abs(end.x - start.x), kotlin.math.abs(end.y - start.y))
                                        if (size.width > 5f && size.height > 5f) {
                                            onElementAdded(
                                                BoardElement.Rectangle(
                                                    topLeft = topLeft,
                                                    size = size,
                                                    isSquare = false,
                                                    color = currentColor,
                                                    strokeWidth = strokeWidth
                                                )
                                            )
                                        }
                                    }
                                    WhiteboardTool.CIRCLE -> {
                                        val center = Offset((start.x + end.x) / 2f, (start.y + end.y) / 2f)
                                        val radius = hypot(end.x - start.x, end.y - start.y) / 2f
                                        if (radius > 5f) {
                                            onElementAdded(
                                                BoardElement.Circle(
                                                    center = center,
                                                    radius = radius,
                                                    color = currentColor,
                                                    strokeWidth = strokeWidth
                                                )
                                            )
                                        }
                                    }
                                    WhiteboardTool.TRIANGLE -> {
                                        val p1 = Offset((start.x + end.x) / 2f, min(start.y, end.y))
                                        val p2 = Offset(min(start.x, end.x), max(start.y, end.y))
                                        val p3 = Offset(max(start.x, end.x), max(start.y, end.y))
                                        onElementAdded(
                                            BoardElement.Triangle(
                                                p1 = p1,
                                                p2 = p2,
                                                p3 = p3,
                                                color = currentColor,
                                                strokeWidth = strokeWidth
                                            )
                                        )
                                    }
                                    else -> {}
                                }
                            }

                            currentPoints.clear()
                            dragStart = null
                            currentDrag = null
                        },
                        onDragCancel = {
                            currentPoints.clear()
                            dragStart = null
                            currentDrag = null
                        }
                    )
                }
        ) {
            // Draw background grid if enabled
            if (showGrid) {
                val step = 32f
                var x = 0f
                while (x < size.width) {
                    drawLine(
                        color = Color(0xFFEFF2FD),
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1f
                    )
                    x += step
                }
                var y = 0f
                while (y < size.height) {
                    drawLine(
                        color = Color(0xFFEFF2FD),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                    y += step
                }
            }

            // Draw saved elements
            for (element in elements) {
                when (element) {
                    is BoardElement.Stroke -> {
                        if (element.points.size >= 2) {
                            val path = Path()
                            path.moveTo(element.points[0].x, element.points[0].y)
                            for (i in 1 until element.points.size) {
                                path.lineTo(element.points[i].x, element.points[i].y)
                            }
                            drawPath(
                                path = path,
                                color = element.color,
                                style = Stroke(
                                    width = element.strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }
                    is BoardElement.Line -> {
                        drawLine(
                            color = element.color,
                            start = element.start,
                            end = element.end,
                            strokeWidth = element.strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                    is BoardElement.Rectangle -> {
                        drawRect(
                            color = element.color,
                            topLeft = element.topLeft,
                            size = element.size,
                            style = Stroke(
                                width = element.strokeWidth,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                    is BoardElement.Circle -> {
                        drawCircle(
                            color = element.color,
                            radius = element.radius,
                            center = element.center,
                            style = Stroke(width = element.strokeWidth)
                        )
                    }
                    is BoardElement.Triangle -> {
                        val path = Path().apply {
                            moveTo(element.p1.x, element.p1.y)
                            lineTo(element.p2.x, element.p2.y)
                            lineTo(element.p3.x, element.p3.y)
                            close()
                        }
                        drawPath(
                            path = path,
                            color = element.color,
                            style = Stroke(
                                width = element.strokeWidth,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                    is BoardElement.Formula -> {
                        // Handled in Compose layout layer below
                    }
                }
            }

            // Draw current active in-progress shape
            val start = dragStart
            val drag = currentDrag
            if (start != null && drag != null) {
                when (currentTool) {
                    WhiteboardTool.PEN -> {
                        if (currentPoints.size >= 2) {
                            val path = Path()
                            path.moveTo(currentPoints[0].x, currentPoints[0].y)
                            for (i in 1 until currentPoints.size) {
                                path.lineTo(currentPoints[i].x, currentPoints[i].y)
                            }
                            drawPath(
                                path = path,
                                color = currentColor,
                                style = Stroke(
                                    width = strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }
                    WhiteboardTool.LINE -> {
                        drawLine(
                            color = currentColor.copy(alpha = 0.85f),
                            start = start,
                            end = drag,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                    WhiteboardTool.RECTANGLE -> {
                        val topLeft = Offset(min(start.x, drag.x), min(start.y, drag.y))
                        val size = Size(kotlin.math.abs(drag.x - start.x), kotlin.math.abs(drag.y - start.y))
                        drawRect(
                            color = currentColor.copy(alpha = 0.85f),
                            topLeft = topLeft,
                            size = size,
                            style = Stroke(width = strokeWidth, join = StrokeJoin.Round)
                        )
                    }
                    WhiteboardTool.CIRCLE -> {
                        val center = Offset((start.x + drag.x) / 2f, (start.y + drag.y) / 2f)
                        val radius = hypot(drag.x - start.x, drag.y - start.y) / 2f
                        drawCircle(
                            color = currentColor.copy(alpha = 0.85f),
                            center = center,
                            radius = radius,
                            style = Stroke(width = strokeWidth)
                        )
                    }
                    WhiteboardTool.TRIANGLE -> {
                        val p1 = Offset((start.x + drag.x) / 2f, min(start.y, drag.y))
                        val p2 = Offset(min(start.x, drag.x), max(start.y, drag.y))
                        val p3 = Offset(max(start.x, drag.x), max(start.y, drag.y))
                        val path = Path().apply {
                            moveTo(p1.x, p1.y)
                            lineTo(p2.x, p2.y)
                            lineTo(p3.x, p3.y)
                            close()
                        }
                        drawPath(
                            path = path,
                            color = currentColor.copy(alpha = 0.85f),
                            style = Stroke(width = strokeWidth, join = StrokeJoin.Round)
                        )
                    }
                    else -> {}
                }
            }
        }

        // Render draggable Formula Stickers
        elements.filterIsInstance<BoardElement.Formula>().forEach { formula ->
            var formulaOffset by remember(formula.id) { mutableStateOf(formula.position) }

            Surface(
                modifier = Modifier
                    .offset { IntOffset(formulaOffset.x.roundToInt(), formulaOffset.y.roundToInt()) }
                    .pointerInput(formula.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val updated = formulaOffset + dragAmount
                            formulaOffset = updated
                            onFormulaMoved(formula.id, updated)
                        }
                    }
                    .pointerInput(formula.id) {
                        detectTapGestures(
                            onDoubleTap = {
                                onElementDeleted(formula)
                            }
                        )
                    },
                shape = RoundedCornerShape(10.dp),
                color = Color.White.copy(alpha = 0.96f),
                shadowElevation = 4.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCDD5F8))
            ) {
                Text(
                    text = formula.text,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = formula.color
                )
            }
        }

        // Recognition Toast badge
        AnimatedVisibility(
            visible = recognitionToast != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF161A2E),
                shadowElevation = 8.dp
            ) {
                Text(
                    text = "Танылды: ${recognitionToast.orEmpty()}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color(0xFF7DF0CE),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

private fun eraseNear(
    point: Offset,
    elements: List<BoardElement>,
    onDelete: (BoardElement) -> Unit
) {
    val eraseRadius = 24f
    for (el in elements) {
        when (el) {
            is BoardElement.Stroke -> {
                if (el.points.any { hypot(it.x - point.x, it.y - point.y) < eraseRadius }) {
                    onDelete(el)
                    return
                }
            }
            is BoardElement.Line -> {
                if (hypot(el.start.x - point.x, el.start.y - point.y) < eraseRadius ||
                    hypot(el.end.x - point.x, el.end.y - point.y) < eraseRadius
                ) {
                    onDelete(el)
                    return
                }
            }
            is BoardElement.Rectangle -> {
                val center = Offset(el.topLeft.x + el.size.width / 2f, el.topLeft.y + el.size.height / 2f)
                if (hypot(center.x - point.x, center.y - point.y) < eraseRadius + el.size.width / 2f) {
                    onDelete(el)
                    return
                }
            }
            is BoardElement.Circle -> {
                if (kotlin.math.abs(hypot(el.center.x - point.x, el.center.y - point.y) - el.radius) < eraseRadius) {
                    onDelete(el)
                    return
                }
            }
            is BoardElement.Triangle -> {
                if (hypot(el.p1.x - point.x, el.p1.y - point.y) < eraseRadius ||
                    hypot(el.p2.x - point.x, el.p2.y - point.y) < eraseRadius ||
                    hypot(el.p3.x - point.x, el.p3.y - point.y) < eraseRadius
                ) {
                    onDelete(el)
                    return
                }
            }
            is BoardElement.Formula -> {
                if (hypot(el.position.x - point.x, el.position.y - point.y) < eraseRadius * 2f) {
                    onDelete(el)
                    return
                }
            }
        }
    }
}

package com.example.rollthedice.cube

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.math.sqrt

fun DrawScope.drawCube(
    offsets: List<Offset>,
    sideColor: Color? = null,
    wireframeColor: Color? = null,
    indexOffset: Int = 0
) {
    if (sideColor != null) {
        val c = listOf(Color.Blue, Color.Green, Color.Cyan, Color.Magenta, Color.Yellow, Color.LightGray, Color.Black);

        // 0, 1, 2, 3
        // 4, 5, 6, 7
        // 0, 3, 7, 4
        // 1, 2, 6, 5
        // 2, 3, 7, 6
        // 0, 1, 5, 4
        visibleFaces(offsets).forEachIndexed { i, it ->
            drawCubeSide(offsets, it, c[i])
        }

        drawPoints(
            points = listOf(offsets[findClosestToCenterIndex(offsets)]),
            color = Color.Magenta,
            pointMode = PointMode.Points,
            strokeWidth = 23f,
            cap = StrokeCap.Round
        )
    }

    if (wireframeColor != null) {
        for (i in 0 until 4) {
            drawLine(
                offsets,
                start = i,
                end = (i + 1) % 4,
                wireframeColor,
                indexOffset
            )
            drawLine(
                offsets,
                start = i + 4,
                end = ((i + 1) % 4) + 4,
                wireframeColor,
                indexOffset
            )
            drawLine(offsets, start = i, end = i + 4, wireframeColor, indexOffset)
        }
    }

}

//fun generateRandomColor(): Color {
//    val r = Random.nextInt(0, 256)
//    val g = Random.nextInt(0, 256)
//    val b = Random.nextInt(0, 256)
//    return Color(r, g, b)
//}
//
//fun generateRandomColorList(n: Int): List<Color> {
//    val colorList = mutableListOf<Color>()
//    repeat(n) {
//        colorList.add(generateRandomColor())
//    }
//    return colorList
//}

fun DrawScope.drawLine(
    offsets: List<Offset>,
    start: Int,
    end: Int,
    color: Color,
    indexOffset: Int = 0
) {
    val startOffset = offsets[start + indexOffset]
    val endOffset = offsets[end + indexOffset]

    drawLine(
        start = startOffset,
        end = endOffset,
        strokeWidth = 5f,
        color = color.copy(alpha = 0.6f),
        cap = StrokeCap.Butt
    )
}

fun DrawScope.drawCubeSide(
    offsets: List<Offset>,
    offsetIndexes: List<Int>,
    color: Color,
) {
    val side = Path().apply {
        offsetIndexes.forEachIndexed{i, n ->
            if (i == 0) {
                moveTo(offsets[n].x, offsets[n].y);
            } else {
                lineTo(offsets[n].x, offsets[n].y);
            }
        }
    }
    drawPath(side, color = color)
}

fun visibleFaces(vertices: List<Offset>): List<List<Int>> {
    val faces = listOf(
        listOf(0, 1, 2, 3),
        listOf(4, 5, 6, 7),
        listOf(0, 3, 7, 4),
        listOf(1, 2, 6, 5),
        listOf(2, 3, 7, 6),
        listOf(0, 1, 5, 4)
    );

//    val vertexIndicesSortedByY = vertices.indices.toList().sortedBy { i -> vertices[i].y }.reversed()
//    Log.d("v", vertices[vertexIndicesSortedByY[0]].toString())
//    var highestVertex = NEGATIVE_INFINITY;
//    for (vertexIndex in vertexIndicesSortedByY) {
//        if (vertices[vertexIndex].y < highestVertex) break;
//    }

    val visibleFacesList = mutableListOf<List<Int>>()
    val centerVertexIndex = findClosestToCenterIndex(vertices)
    visibleFacesList.addAll(faces.filter { it.contains(centerVertexIndex) })
//    highestVertex = visibleFacesList.flatten().map { vertices[it] }.maxByOrNull { it.y }!!.y.toDouble()

    return visibleFacesList
}


fun findClosestToCenterIndex(offsets: List<Offset>): Int {
    val center = Offset(0f, 0f) // Center of the coordinate system

    // Find the index of the offset closest to the center
    var closestIndex = 0
    var closestDistance = distance(center, offsets[0])

    for (i in 1 until offsets.size) {
        val distance = distance(center, offsets[i])
        if (distance < closestDistance) {
            closestIndex = i
            closestDistance = distance
        }
    }

    return closestIndex
}

fun distance(offset1: Offset, offset2: Offset): Float {
    val dx = offset1.x - offset2.x
    val dy = offset1.y - offset2.y
    return sqrt(dx * dx + dy * dy)
}
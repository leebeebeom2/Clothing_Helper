package com.leebeebeom.clothinghelper.main.detail

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.graphics.PathParser
import java.util.regex.Pattern

class FolderShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path =
            "M4,15.729Q3.417,15.729 3.021,15.333Q2.625,14.938 2.625,14.375V6.333Q2.625,5.771 3.021,5.375Q3.417,4.979 4,4.979H8.375Q8.667,4.979 8.927,5.094Q9.188,5.208 9.375,5.396L10,6.021H16Q16.583,6.021 16.979,6.417Q17.375,6.812 17.375,7.375V14.375Q17.375,14.938 16.979,15.333Q16.583,15.729 16,15.729Z"
        val scaleX = size.width / 18
        val scaleY = size.height / 18
        return Outline.Generic(
            PathParser.createPathFromPathData(resize(path, scaleX, scaleY)).asComposePath()
        )
    }

    private fun resize(pathData: String, scaleX: Float, scaleY: Float): String {
        val matcher = Pattern.compile("[0-9]+[.]?([0-9]+)?")
            .matcher(pathData) // match the numbers in the path
        val stringBuffer = StringBuffer()
        var count = 0
        while (matcher.find()) {
            val number = matcher.group().toFloat()
            matcher.appendReplacement(
                stringBuffer,
                (if (count % 2 == 0) number * scaleX else number * scaleY).toString()
            ) // replace numbers with scaled numbers
            ++count
        }
        return stringBuffer.toString() // return the scaled path
    }
}


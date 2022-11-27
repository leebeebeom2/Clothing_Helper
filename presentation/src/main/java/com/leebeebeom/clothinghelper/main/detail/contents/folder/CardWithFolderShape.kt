package com.leebeebeom.clothinghelper.main.detail.contents.folder

import android.graphics.Matrix
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardWithFolderShape(
    size: Dp,
    elevation: Dp,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        shape = FolderShape,
        modifier = Modifier
            .size(size)
            .aspectRatio(1.4f),
        elevation = elevation,
        onClick = onClick,
        content = content
    )
}

val FolderShape: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val baseWidth = 34.8f
        val baseHeight = 24.8f

        val path = Path()

        path.moveTo(2.7f, 24.8f)
        path.cubicTo(1.9667f, 24.8f, 1.3333f, 24.5333f, 0.8f, 24f)
        path.cubicTo(0.2667f, 23.4667f, 0f, 22.8333f, 0f, 22.1f)
        path.lineTo(0f, 2.7f)
        path.cubicTo(0f, 1.9667f, 0.2667f, 1.3333f, 0.8f, 0.8f)
        path.cubicTo(1.3333f, 0.2667f, 1.9667f, 0f, 2.7f, 0f)
        path.lineTo(14.6f, 0f)
        path.cubicTo(14.9667f, 0f, 15.3167f, 0.0667f, 15.65f, 0.2f)
        path.cubicTo(15.9833f, 0.3333f, 16.2833f, 0.5333f, 16.55f, 0.8f)
        path.lineTo(17.4f, 1.7f)
        path.lineTo(32.1f, 1.7f)
        path.cubicTo(32.8333f, 1.7f, 33.4667f, 1.9667f, 34f, 2.5f)
        path.cubicTo(34.5333f, 3.0333f, 34.8f, 3.6667f, 34.8f, 4.4f)
        path.lineTo(34.8f, 22.1f)
        path.cubicTo(34.8f, 22.8333f, 34.5333f, 23.4667f, 34f, 24f)
        path.cubicTo(33.4667f, 24.5333f, 32.8333f, 24.8f, 32.1f, 24.8f)
        path.lineTo(2.7f, 24.8f)
        path.close()

        return Outline.Generic(
            path
                .asAndroidPath()
                .apply {
                    transform(Matrix().apply {
                        setScale(size.width / baseWidth, size.height / baseHeight)
                    })
                }
                .asComposePath()
        )
    }
}

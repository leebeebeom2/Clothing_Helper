package com.leebeebeom.clothinghelper.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = Black,
    primaryVariant = Black,
    secondary = Teal200,
    background = WhiteSmoke
)

@Composable // skippable
fun ClothingHelperTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Type,
        shapes = Shapes,
        content = content,
    )
}
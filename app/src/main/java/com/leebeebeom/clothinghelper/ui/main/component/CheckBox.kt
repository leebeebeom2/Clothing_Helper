package com.leebeebeom.clothinghelper.ui.main.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.AnimatedIconWrapper
import com.leebeebeom.clothinghelper.ui.util.noRippleClickable

@Composable
fun SquareCheckBox( // skippable
    modifier: Modifier = Modifier,
    isChecked: () -> Boolean,
    onClick: () -> Unit,
    size: Dp
) {
    AnimatedIconWrapper(
        modifier =
        modifier
            .size(size)
            .noRippleClickable { onClick() }, drawable = R.drawable.check_anime_square,
        atEnd = isChecked,
        tint = checkBoxIconTint()
    )
}

@Composable
fun checkBoxIconTint() = LocalContentColor.current.copy(0.7f)
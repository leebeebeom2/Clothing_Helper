package com.leebeebeom.clothinghelper.base.composables

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SimpleIcon(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = drawable),
        contentDescription = null,
        tint = tint
    )
}

@Composable
fun SimpleHeightSpacer(dp: Int) {
    Spacer(modifier = Modifier.height(dp.dp))
}

@Composable
fun SimpleWidthSpacer(dp: Int) {
    Spacer(modifier = Modifier.width(dp.dp))
}

@Composable
fun SimpleToast(@StringRes text: () -> Int?, toastShown: () -> Unit) {
    text()?.let {
        Toast.makeText(LocalContext.current, stringResource(id = it), Toast.LENGTH_SHORT).show()
        toastShown()
    }
}
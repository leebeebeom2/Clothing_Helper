package com.leebeebeom.clothinghelper.ui.component

import androidx.annotation.StringRes
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable // skippable
fun SingleLineText(
    text: () -> String?,
    style: TextStyle = LocalTextStyle.current
) {
    SingleLineText(text = text(), style = style)
}

@Composable // skippable
fun SingleLineText(
    modifier: Modifier = Modifier,
    @StringRes text: Int?,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign? = null
) {
    text?.let {
        SingleLineText(
            modifier = modifier,
            text = stringResource(id = text),
            style = style,
            textAlign = textAlign
        )
    }
}

@Composable // skippable
fun SingleLineText(
    modifier: Modifier = Modifier,
    text: String?,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign? = null
) {
    text?.let {
        Text(
            modifier = modifier,
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = style,
            textAlign = textAlign
        )
    }
}
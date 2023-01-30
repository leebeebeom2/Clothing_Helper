package com.leebeebeom.clothinghelper.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun SingleLineText(
    modifier: Modifier = Modifier,
    text: () -> String?,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign? = null
) {
    SingleLineText(modifier = modifier, text = text(), style = style, textAlign = textAlign)
}

@Composable
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

@Composable
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
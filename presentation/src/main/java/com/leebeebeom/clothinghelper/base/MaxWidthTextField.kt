package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import kotlinx.coroutines.delay

@Composable
fun MaxWidthTextField(
    @StringRes label: Int,
    @StringRes placeholder: Int = R.string.empty,
    text: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    @StringRes error: Int? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    showKeyboardEnabled: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    onFocusChanged: (FocusState) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = focusRequester)
                .onFocusChanged(onFocusChanged = onFocusChanged),
            value = text,
            onValueChange = onValueChange,
            label = { Text(text = stringResource(id = label)) },
            placeholder = { Text(text = stringResource(id = placeholder)) },
            isError = error != null,
            visualTransformation = visualTransformation,
            singleLine = true,
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingIcon,
            keyboardActions =
            if (keyboardOptions.imeAction == ImeAction.Done) KeyboardActions(onDone = { focusManager.clearFocus() })
            else KeyboardActions.Default,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFDADADA),
                unfocusedLabelColor = Color(0xFF8391A1),
                backgroundColor = Color(0xFFF7F8F9),
                placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
            )
        )
        ErrorText(error)
    }
    if (showKeyboardEnabled) ShowKeyboard(focusRequester)
}


@Composable
private fun ErrorText(@StringRes error: Int?) {
    AnimatedVisibility(
        visible = error != null,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            expandFrom = Alignment.Bottom
        ),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 50),
            shrinkTowards = Alignment.Top
        )
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            text = error?.let { stringResource(id = it) } ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowKeyboard(focusRequester: FocusRequester) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        delay(100)
        keyboardController?.show()
    }
}
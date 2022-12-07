package com.leebeebeom.clothinghelper.main.base.composables.sizechartcomposables

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.base.composables.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.composables.rememberMaxWidthTextFieldState

@Composable
fun SizeChartTemplateTextField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    textFieldValue: TextFieldValue = TextFieldValue()
) {
    SizeChartTextFiled(
        modifier = modifier,
        label = label,
        textFieldValue = textFieldValue,
        onValueChange = {},
        enabled = false
    )
}

@Composable
fun SizeChartEssentialTextField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    imeAction: ImeAction = ImeAction.Next
) {
    SizeChartTextFiled(
        modifier = modifier,
        label = label,
        textFieldValue = textFieldValue,
        onValueChange = onValueChange,
        keyboardType = KeyboardType.Number,
        imeAction = imeAction
    )
}

@Composable
fun SizeChartTextFiled(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true
) {
    MaxWidthTextField(
        modifier = modifier,
        state = rememberSizeChartTexFieldState(
            label = label,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)
        ),
        textFieldValue = { textFieldValue },
        error = { null },
        onValueChange = onValueChange,
        onFocusChanged = {},
        cancelIconEnabled = false,
        enabled = enabled
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberSizeChartTexFieldState(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions
) = rememberMaxWidthTextFieldState(
    label = label,
    keyboardOptions = keyboardOptions
)
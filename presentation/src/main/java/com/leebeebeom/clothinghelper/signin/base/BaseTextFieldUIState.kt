package com.leebeebeom.clothinghelper.signin.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue

private fun baseOnTextChange(
    _text: MutableState<TextFieldValue>,
    text: TextFieldValue,
    hideTextError: () -> Unit
) {
    _text.value = text
    hideTextError()
}

open class OneTextFiledState(text: String = "") {
    protected open val text = mutableStateOf(TextFieldValue(text))

    protected val onTextChange = { newText: TextFieldValue, hideTextError: () -> Unit ->
        baseOnTextChange(this.text, newText, hideTextError)
    }
}

open class TwoTextFiledState(text: String = "", text2: String = "") :
    OneTextFiledState(text) {
    protected val text2 = mutableStateOf(TextFieldValue(text2))

    protected val onText2Change = { newText2: TextFieldValue, hideText2Error: () -> Unit ->
        baseOnTextChange(this.text2, newText2, hideText2Error)
    }
}

open class FourTextFieldState(
    text: String = "",
    text2: String = "",
    text3: String = "",
    text4: String = ""
) : TwoTextFiledState(text, text2) {
    protected val text3 = mutableStateOf(TextFieldValue(text3))

    protected val text4 = mutableStateOf(TextFieldValue(text4))

    protected val onText3Change = { newText3: TextFieldValue, hideText3Error: () -> Unit ->
        baseOnTextChange(this.text3, newText3, hideText3Error)
    }

    protected val onText4Change = { newText4: TextFieldValue, hideText4Error: () -> Unit ->
        baseOnTextChange(this.text4, newText4, hideText4Error)
    }
}
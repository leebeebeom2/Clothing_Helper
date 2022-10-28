package com.leebeebeom.clothinghelper.signin.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

private fun baseOnTextChange(_text: MutableState<String>, text: String, hideTextError: () -> Unit) {
    _text.value = text
    hideTextError()
}

open class OneTextFiledState(text: String = "") {
    protected val text = mutableStateOf(text)

    protected val onTextChange = { newText: String, hideTextError: () -> Unit ->
        baseOnTextChange(this.text, newText, hideTextError)
    }
}

open class TwoTextFiledState(text: String = "", text2: String = "") :
    OneTextFiledState(text) {
    protected val text2 = mutableStateOf(text2)

    protected val onText2Change = { newText2: String, hideText2Error: () -> Unit ->
        baseOnTextChange(this.text2, newText2, hideText2Error)
    }
}

open class FourTextFieldState(
    text: String = "",
    text2: String = "",
    text3: String = "",
    text4: String = ""
) : TwoTextFiledState(text, text2) {
    protected val text3 = mutableStateOf(text3)

    protected val text4 = mutableStateOf(text4)

    protected val onText3Change = { newText3: String, hideText3Error: () -> Unit ->
        baseOnTextChange(this.text3, newText3, hideText3Error)
    }

    protected val onText4Change = { newText4: String, hideText4Error: () -> Unit ->
        baseOnTextChange(this.text4, newText4, hideText4Error)
    }
}
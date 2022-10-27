package com.leebeebeom.clothinghelper.signin.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

private fun baseOnTextChange(_text: MutableState<String>, text: String, hideTextError: () -> Unit) {
    _text.value = text
    hideTextError()
}

open class OneTextFiledState(text: String = "") {
    private val _text = mutableStateOf(text)
    open val text get() = _text.value

    val onTextChange = { newText: String, hideTextError: () -> Unit ->
        baseOnTextChange(_text, newText, hideTextError)
    }
}

open class TwoTextFiledState(text: String = "", text2: String = "") :
    OneTextFiledState(text) {
    private val _text2 = mutableStateOf(text2)
    open val text2 get() = _text2.value

    val onText2Change = { newText2: String, hideText2Error: () -> Unit ->
        baseOnTextChange(_text2, newText2, hideText2Error)
    }
}

open class FourTextFieldState(
    text: String = "",
    text2: String = "",
    text3: String = "",
    text4: String = ""
) : TwoTextFiledState(text, text2) {
    private val _text3 = mutableStateOf(text3)
    open val text3 get() = _text3.value

    private val _text4 = mutableStateOf(text4)
    open val text4 get() = _text4.value

    val onText3Change = { newText3: String, hideText3Error: () -> Unit ->
        baseOnTextChange(_text3, newText3, hideText3Error)
    }

    val onText4Change = { newText4: String, hideText4Error: () -> Unit ->
        baseOnTextChange(_text4, newText4, hideText4Error)
    }
}
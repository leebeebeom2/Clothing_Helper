package com.leebeebeom.clothinghelper

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.test.platform.app.InstrumentationRegistry

fun isKeyboardShowing(): Boolean {
    val inputMethodManager =
        InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    KeyboardType
    return inputMethodManager.isAcceptingText
}
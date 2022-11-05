package com.leebeebeom.clothinghelper.base

import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester

interface ClearFocus {
    val focusManager: FocusManager

    fun clearFocus() = focusManager.clearFocus()
}

interface RequestFocus {
    val focusRequester: FocusRequester

    fun requestFocus() = focusRequester.requestFocus()
}
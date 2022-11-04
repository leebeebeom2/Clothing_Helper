package com.leebeebeom.clothinghelper.base

import androidx.compose.ui.focus.FocusManager

interface ClearFocus {
    val focusManager:FocusManager

    fun clearFocus() = focusManager.clearFocus()
}
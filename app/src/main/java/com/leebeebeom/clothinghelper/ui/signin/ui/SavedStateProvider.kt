package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle

class SavedStateProvider<T : Any?>(
    savedStateHandle: SavedStateHandle, key: String, initialValue: T
) {
    var savedValue by SavedStateHandleDelegator(
        savedStateHandle = savedStateHandle, key = key, initial = initialValue
    )
        private set
    var state by mutableStateOf(savedValue)
        private set
    val flow = snapshotFlow { state }

    fun set(value: T) {
        if (savedValue == value) return
        savedValue = value
        state = value
    }
}
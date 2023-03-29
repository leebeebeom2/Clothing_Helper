package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable

@OptIn(SavedStateHandleSaveableApi::class)
class SavedStateProvider<T : Any?>(
    savedStateHandle: SavedStateHandle, key: String, initialValue: T
) {
    var state by savedStateHandle.saveable(key) { mutableStateOf(initialValue) }
        private set
    val flow = snapshotFlow { state }

    fun set(value: T) {
        state = value
    }
}
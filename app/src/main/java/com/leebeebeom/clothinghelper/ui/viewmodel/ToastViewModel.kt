package com.leebeebeom.clothinghelper.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.collections.immutable.toImmutableList

@OptIn(SavedStateHandleSaveableApi::class)
abstract class ToastViewModel(
    savedToastTextsKey: String,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val toastTexts =
        savedStateHandle.saveable(
            key = savedToastTextsKey,
            saver = listSaver<SnapshotStateList<Int>, Int>(
                save = { it },
                restore = { it.toMutableStateList() }
            )
        ) { mutableStateListOf() }
    protected val toastTextsFlow = snapshotFlow { toastTexts.toImmutableList() }

    fun addToastTextAtLast(toastText: Int) {
        toastTexts.add(toastText)
    }

    fun removeFirstToastText() {
        toastTexts.removeFirst()
    }
}
package com.leebeebeom.clothinghelper.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList

abstract class ToastViewModel : ViewModel() {
    private val toastTexts = mutableStateListOf<Int>()
    protected val toastTextsFlow = snapshotFlow { toastTexts.toImmutableList() }

    fun addToastTextAtLast(toastText: Int) {
        toastTexts.add(toastText)
    }

    fun removeFirstToastText() {
        toastTexts.removeFirst()
    }
}
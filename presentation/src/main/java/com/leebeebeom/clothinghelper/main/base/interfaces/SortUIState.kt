package com.leebeebeom.clothinghelper.main.base.interfaces

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelperdomain.model.SortPreferences

interface SortUIState {
    val sort: SortPreferences

    fun updateSort(sort: SortPreferences)
}

class SortUIStateImpl : SortUIState {
    override var sort by mutableStateOf(SortPreferences())
        private set

    override fun updateSort(sort: SortPreferences) {
        this.sort = sort
    }
}
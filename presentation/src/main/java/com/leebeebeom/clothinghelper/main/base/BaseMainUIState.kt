package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.leebeebeom.clothinghelper.base.BaseUIState
import com.leebeebeom.clothinghelperdomain.model.SubCategory

open class BaseMainUIState : BaseUIState() {
    private val _isLoading = mutableStateOf(false)
    private val _allSubCategories = mutableStateOf(List(4) { emptyList<SubCategory>() })

    val isLoading by derivedStateOf { _isLoading.value }
    val allSubCategories by derivedStateOf { _allSubCategories.value }


    fun updateIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun updateAllSubCategories(allSubCategories: List<List<SubCategory>>) {
        _allSubCategories.value = allSubCategories
    }
}
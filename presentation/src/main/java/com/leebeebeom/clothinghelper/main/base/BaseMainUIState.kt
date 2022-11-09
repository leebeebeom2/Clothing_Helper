package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.leebeebeom.clothinghelper.base.BaseUIState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

open class BaseMainUIState : BaseUIState() {
    private val _isLoading = mutableStateOf(false)
    private val _allSubCategories = mutableStateOf(List(4) { emptyList<SubCategory>() })

    val isLoading by derivedStateOf { _isLoading.value }
    val allSubCategories by derivedStateOf { _allSubCategories.value }

    fun subCategoriesSize(subCategoryParent: SubCategoryParent) =
        derivedStateOf { allSubCategories[subCategoryParent.ordinal].size }

    fun updateIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun updateAllSubCategories(allSubCategories: List<List<SubCategory>>) {
        _allSubCategories.value = allSubCategories
    }
}

open class BaseIsAllExpandState : BaseMainUIState() {
    private val _isAllExpand = mutableStateOf(false)

    val isAllExpand by derivedStateOf { _isAllExpand.value }

    fun updateIsAllExpand(isAllExpand: Boolean) {
        _isAllExpand.value = isAllExpand
    }
}
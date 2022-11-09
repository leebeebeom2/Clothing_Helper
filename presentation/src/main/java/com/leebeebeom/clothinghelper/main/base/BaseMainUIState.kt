package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.leebeebeom.clothinghelper.base.BaseUIState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User

open class BaseMainUIState : BaseUIState() {
    private val _isLoading = mutableStateOf(false)
    private val _allSubCategories = mutableStateOf(List(4) { emptyList<SubCategory>() })

    val isLoading by derivedStateOf { _isLoading.value }
    val allSubCategories by derivedStateOf { _allSubCategories.value }

    private val topSubCategories by derivedStateOf { _allSubCategories.value[0] }
    private val bottomSubCategories by derivedStateOf { _allSubCategories.value[1] }
    private val outerSubCategories by derivedStateOf { _allSubCategories.value[2] }
    private val etcSubCategories by derivedStateOf { _allSubCategories.value[3] }

    private val topSubCategoriesSize by derivedStateOf { topSubCategories.size }
    private val bottomSubCategoriesSize by derivedStateOf { bottomSubCategories.size }
    private val outerSubCategoriesSize by derivedStateOf { outerSubCategories.size }
    private val etcSubCategoriesSize by derivedStateOf { etcSubCategories.size }

    fun getSubCategories(subCategoryParent: SubCategoryParent) =
        when (subCategoryParent) {
            SubCategoryParent.TOP -> topSubCategories
            SubCategoryParent.BOTTOM -> bottomSubCategories
            SubCategoryParent.OUTER -> outerSubCategories
            SubCategoryParent.ETC -> etcSubCategories
        }

    fun subCategoriesSize(subCategoryParent: SubCategoryParent) =
        when (subCategoryParent) {
            SubCategoryParent.TOP -> topSubCategoriesSize
            SubCategoryParent.BOTTOM -> bottomSubCategoriesSize
            SubCategoryParent.OUTER -> outerSubCategoriesSize
            SubCategoryParent.ETC -> etcSubCategoriesSize
        }

    fun updateIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun updateAllSubCategories(allSubCategories: List<List<SubCategory>>) {
        _allSubCategories.value = allSubCategories
    }
}

open class BaseIsAllExpandState : BaseMainUIState() {
    private val _user: MutableState<User?> = mutableStateOf(null)
    private val _isAllExpand = mutableStateOf(false)

    val user by derivedStateOf { _user.value }
    val isAllExpand by derivedStateOf { _isAllExpand.value }

    fun updateUser(user: User?) {
        _user.value = user
    }

    fun updateIsAllExpand(isAllExpand: Boolean) {
        _isAllExpand.value = isAllExpand
    }
}
package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.*
import com.leebeebeom.clothinghelper.base.BaseUIState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User

open class BaseMainUIState : BaseUIState() {
    var isLoading by mutableStateOf(false)
        private set
    var allSubCategories by mutableStateOf(List(4) { emptyList<SubCategory>() })
        private set

    private val topSubCategories by derivedStateOf { allSubCategories[0] }
    private val bottomSubCategories by derivedStateOf { allSubCategories[1] }
    private val outerSubCategories by derivedStateOf { allSubCategories[2] }
    private val etcSubCategories by derivedStateOf { allSubCategories[3] }

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
        this.isLoading = isLoading
    }

    fun updateAllSubCategories(allSubCategories: List<List<SubCategory>>) {
        this.allSubCategories = allSubCategories
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
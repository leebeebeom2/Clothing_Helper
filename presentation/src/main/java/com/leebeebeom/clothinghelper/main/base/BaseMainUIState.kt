package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.base.BaseUIState
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

open class BaseMainUIState : BaseUIState() {
    var isLoading by mutableStateOf(false)
        private set
    var allSubCategories by mutableStateOf(List(4) { emptyList<SubCategory>() })
        private set

    private val topSubCategories by derivedStateOf {
        allSubCategories[0].map { it.toStable() }.toImmutableList()
    }
    private val bottomSubCategories by derivedStateOf {
        allSubCategories[1].map { it.toStable() }.toImmutableList()
    }
    private val outerSubCategories by derivedStateOf {
        allSubCategories[2].map { it.toStable() }.toImmutableList()
    }
    private val etcSubCategories by derivedStateOf {
        allSubCategories[3].map { it.toStable() }.toImmutableList()
    }

    private val topSubCategoriesSize by derivedStateOf { topSubCategories.size }
    private val bottomSubCategoriesSize by derivedStateOf { bottomSubCategories.size }
    private val outerSubCategoriesSize by derivedStateOf { outerSubCategories.size }
    private val etcSubCategoriesSize by derivedStateOf { etcSubCategories.size }

    private val topSubCategoryNames by derivedStateOf {
        topSubCategories.map { it.name }.toImmutableList()
    }
    private val bottomSubCategoryNames by derivedStateOf {
        bottomSubCategories.map { it.name }.toImmutableList()
    }
    private val outerSubCategoryNames by derivedStateOf {
        outerSubCategories.map { it.name }.toImmutableList()
    }
    private val etcSubCategoryNames by derivedStateOf {
        etcSubCategories.map { it.name }.toImmutableList()
    }

    fun getSubCategories(subCategoryParent: SubCategoryParent): ImmutableList<StableSubCategory> {
        return when (subCategoryParent) {
            SubCategoryParent.TOP -> topSubCategories
            SubCategoryParent.BOTTOM -> bottomSubCategories
            SubCategoryParent.OUTER -> outerSubCategories
            SubCategoryParent.ETC -> etcSubCategories
        }
    }

    fun getSubCategoriesSize(subCategoryParent: SubCategoryParent): Int {
        return when (subCategoryParent) {
            SubCategoryParent.TOP -> topSubCategoriesSize
            SubCategoryParent.BOTTOM -> bottomSubCategoriesSize
            SubCategoryParent.OUTER -> outerSubCategoriesSize
            SubCategoryParent.ETC -> etcSubCategoriesSize
        }
    }

    fun getSubCategoryNames(subCategoryParent: SubCategoryParent): ImmutableList<String> {
        return when (subCategoryParent) {
            SubCategoryParent.TOP -> topSubCategoryNames
            SubCategoryParent.BOTTOM -> bottomSubCategoryNames
            SubCategoryParent.OUTER -> outerSubCategoryNames
            SubCategoryParent.ETC -> etcSubCategoryNames
        }
    }

    fun updateIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun updateAllSubCategories(allSubCategories: List<List<SubCategory>>) {
        this.allSubCategories = allSubCategories
    }
}

open class BaseIsAllExpandState : BaseMainUIState() {
    var isAllExpand by mutableStateOf(false)
        private set

    fun updateIsAllExpand(isAllExpand: Boolean) {
        this.isAllExpand = isAllExpand
    }
}
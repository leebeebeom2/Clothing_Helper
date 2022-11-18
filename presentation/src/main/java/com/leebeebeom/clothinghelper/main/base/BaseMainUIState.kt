package com.leebeebeom.clothinghelper.main.base

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.base.BaseUIState
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.container.SubCategory
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

abstract class BaseMainUIState : BaseUIState() {
    var isLoading by mutableStateOf(false)
        private set
    private var allSubCategories by mutableStateOf(emptyList<StableSubCategory>())

    private val topSubCategories by derivedStateOf {
        allSubCategories.filter { it.parent == SubCategoryParent.TOP }.toImmutableList()
    }
    private val bottomSubCategories by derivedStateOf {
        allSubCategories.filter { it.parent == SubCategoryParent.BOTTOM }.toImmutableList()
    }
    private val outerSubCategories by derivedStateOf {
        allSubCategories.filter { it.parent == SubCategoryParent.OUTER }.toImmutableList()
    }
    private val etcSubCategories by derivedStateOf {
        allSubCategories.filter { it.parent == SubCategoryParent.ETC }.toImmutableList()
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

    fun loadAllSubCategories(allSubCategories: List<SubCategory>) {
        this.allSubCategories = allSubCategories.map { it.toStable() }
    }
}
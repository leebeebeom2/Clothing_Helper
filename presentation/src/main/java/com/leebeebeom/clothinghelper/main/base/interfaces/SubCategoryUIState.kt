package com.leebeebeom.clothinghelper.main.base.interfaces

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.container.SubCategory
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

interface SubCategoryUIState {
    val allSubCategories: List<StableSubCategory>

    val topSubCategories: ImmutableList<StableSubCategory>
    val bottomSubCategories: ImmutableList<StableSubCategory>
    val outerSubCategories: ImmutableList<StableSubCategory>
    val etcSubCategories: ImmutableList<StableSubCategory>

    val topSubCategoriesSize: Int
    val bottomSubCategoriesSize: Int
    val outerSubCategoriesSize: Int
    val etcSubCategoriesSize: Int

    val topSubCategoryNames: ImmutableList<String>
    val bottomSubCategoryNames: ImmutableList<String>
    val outerSubCategoryNames: ImmutableList<String>
    val etcSubCategoryNames: ImmutableList<String>

    fun loadAllSubCategories(allSubCategories: List<SubCategory>)

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
}

class SubCategoryUIStateImpl : SubCategoryUIState {
    override var allSubCategories by mutableStateOf(emptyList<StableSubCategory>())
        private set

    override val topSubCategories by derivedStateOf {
        allSubCategories.filter { it.parent == SubCategoryParent.TOP }.toImmutableList()
    }
    override val bottomSubCategories by derivedStateOf {
        allSubCategories.filter { it.parent == SubCategoryParent.BOTTOM }.toImmutableList()
    }
    override val outerSubCategories by derivedStateOf {
        allSubCategories.filter { it.parent == SubCategoryParent.OUTER }.toImmutableList()
    }
    override val etcSubCategories by derivedStateOf {
        allSubCategories.filter { it.parent == SubCategoryParent.ETC }.toImmutableList()
    }

    override val topSubCategoriesSize by derivedStateOf { topSubCategories.size }
    override val bottomSubCategoriesSize by derivedStateOf { bottomSubCategories.size }
    override val outerSubCategoriesSize by derivedStateOf { outerSubCategories.size }
    override val etcSubCategoriesSize by derivedStateOf { etcSubCategories.size }

    override val topSubCategoryNames by derivedStateOf {
        topSubCategories.map { it.name }.toImmutableList()
    }
    override val bottomSubCategoryNames by derivedStateOf {
        bottomSubCategories.map { it.name }.toImmutableList()
    }
    override val outerSubCategoryNames by derivedStateOf {
        outerSubCategories.map { it.name }.toImmutableList()
    }
    override val etcSubCategoryNames by derivedStateOf {
        etcSubCategories.map { it.name }.toImmutableList()
    }

    override fun loadAllSubCategories(allSubCategories: List<SubCategory>) {
        this.allSubCategories = allSubCategories.map { it.toStable() }
    }
}
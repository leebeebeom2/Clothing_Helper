package com.leebeebeom.clothinghelper.main.base.interfaces

import androidx.compose.runtime.*
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.data.SubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

interface SubCategoryUIState {
    val allSubCategories: List<StableSubCategory>

    fun loadAllSubCategories(allSubCategories: List<SubCategory>)

    fun getSubCategories(subCategoryParent: SubCategoryParent): ImmutableList<StableSubCategory>
}

class SubCategoryUIStateImpl : SubCategoryUIState {
    override var allSubCategories by mutableStateOf(emptyList<StableSubCategory>())
        private set
    private val subCategoriesMap =
        hashMapOf<SubCategoryParent, State<ImmutableList<StableSubCategory>>>()

    override fun loadAllSubCategories(allSubCategories: List<SubCategory>) {
        this.allSubCategories = allSubCategories.map { it.toStable() }
    }

    override fun getSubCategories(subCategoryParent: SubCategoryParent): ImmutableList<StableSubCategory> {
        if (subCategoriesMap[subCategoryParent] == null) {
            subCategoriesMap[subCategoryParent] = derivedStateOf {
                allSubCategories.filter { it.parent == subCategoryParent }.toImmutableList()
            }
        }
        return subCategoriesMap[subCategoryParent]?.value!!
    }
}
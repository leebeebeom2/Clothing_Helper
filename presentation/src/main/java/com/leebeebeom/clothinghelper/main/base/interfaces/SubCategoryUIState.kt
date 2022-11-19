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

    fun loadAllSubCategories(allSubCategories: List<SubCategory>)

    fun getSubCategories(subCategoryParent: SubCategoryParent): ImmutableList<StableSubCategory>
}

class SubCategoryUIStateImpl : SubCategoryUIState {
    override var allSubCategories by mutableStateOf(emptyList<StableSubCategory>())
        private set

    override fun loadAllSubCategories(allSubCategories: List<SubCategory>) {
        this.allSubCategories = allSubCategories.map { it.toStable() }
    }

    override fun getSubCategories(subCategoryParent: SubCategoryParent): ImmutableList<StableSubCategory> =
        derivedStateOf {
            allSubCategories.filter { it.parent == subCategoryParent }.toImmutableList()
        }.value
}
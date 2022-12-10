package com.leebeebeom.clothinghelper.ui.main.interfaces.container

import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.data.SubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

interface SubCategoryUIState {
    val allSubCategories: List<StableSubCategory>

    fun loadAllSubCategories(allSubCategories: List<SubCategory>)
    fun getSubCategories(parentName: String): ImmutableList<StableSubCategory>
}

class SubCategoryUIStateImpl : SubCategoryUIState,
    ContainerUIState<StableSubCategory> by ContainerUIStateImpl() {

    override val allSubCategories get() = allItems

    override fun loadAllSubCategories(allSubCategories: List<SubCategory>) =
        load(allSubCategories.map { it.toStable() })

    override fun getSubCategories(parentName: String) =
        getItems(parentName) { it.parent == enumValueOf<SubCategoryParent>(parentName) }
}
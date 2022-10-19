package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.data.model.SubCategory
import com.leebeebeom.clothinghelper.data.model.SubCategoryParent
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    var allSubCategories: StateFlow<List<SubCategory>>
    var topSubCategories: StateFlow<List<SubCategory>>
    var bottomSubCategories: StateFlow<List<SubCategory>>
    var outerSubCategories: StateFlow<List<SubCategory>>
    var etcSubCategories: StateFlow<List<SubCategory>>

    fun writeInitialSubCategory()
    fun addSubCategory(subCategoryParent: SubCategoryParent, name: String)
}
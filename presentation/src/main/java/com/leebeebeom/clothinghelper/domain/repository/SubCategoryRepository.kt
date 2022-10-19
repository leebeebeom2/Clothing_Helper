package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.data.model.SubCategory
import com.leebeebeom.clothinghelper.data.model.SubCategoryParent
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    val allSubCategories: StateFlow<List<SubCategory>>
    val topSubCategories: StateFlow<List<SubCategory>>
    val bottomSubCategories: StateFlow<List<SubCategory>>
    val outerSubCategories: StateFlow<List<SubCategory>>
    val etcSubCategories: StateFlow<List<SubCategory>>

    fun writeInitialSubCategory()
    fun addSubCategory(subCategoryParent: SubCategoryParent, name: String)
}
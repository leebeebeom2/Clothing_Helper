package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
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
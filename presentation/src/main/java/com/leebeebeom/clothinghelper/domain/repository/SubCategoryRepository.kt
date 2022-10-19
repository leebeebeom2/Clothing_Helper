package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.data.model.SubCategory
import com.leebeebeom.clothinghelper.data.model.SubCategoryParent
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    fun writeInitialSubCategory()
    fun addSubCategory(subCategoryParent:SubCategoryParent, name:String)
    fun getAllSubCategory():StateFlow<List<SubCategory>>
}
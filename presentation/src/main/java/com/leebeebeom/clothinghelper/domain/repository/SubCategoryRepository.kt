package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.data.model.SubCategoryParent

interface SubCategoryRepository {
    fun writeInitialSubCategory()
    fun addSubCategory(subCategoryParent:SubCategoryParent, name:String)
}
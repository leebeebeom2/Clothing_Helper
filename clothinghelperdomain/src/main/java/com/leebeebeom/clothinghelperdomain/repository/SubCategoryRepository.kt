package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.container.SubCategory
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    val isLoading: StateFlow<Boolean>
    val allSubCategories: StateFlow<List<SubCategory>>

    suspend fun loadSubCategories(uid: String): FirebaseResult
    suspend fun pushInitialSubCategories(uid: String)
    suspend fun addSubCategory(subCategory: SubCategory): FirebaseResult
    suspend fun editSubCategoryName(newSubCategory: SubCategory): FirebaseResult
}
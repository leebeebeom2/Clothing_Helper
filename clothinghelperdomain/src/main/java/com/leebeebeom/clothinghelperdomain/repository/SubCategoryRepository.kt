package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.*
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    val isLoading: StateFlow<Boolean>
    val allSubCategories: StateFlow<List<List<SubCategory>>>

    suspend fun loadSubCategories(user: User?): FirebaseResult

    suspend fun pushInitialSubCategories(uid: String): SubCategoryPushResult

    suspend fun addSubCategory(
        subCategoryParent: SubCategoryParent,
        name: String,
        uid: String
    ): FirebaseResult

    suspend fun editSubCategoryName(
        parent: SubCategoryParent,
        key: String,
        newName: String,
        uid: String
    ): FirebaseResult
}
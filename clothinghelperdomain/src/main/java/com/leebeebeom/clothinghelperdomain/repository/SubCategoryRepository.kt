package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    suspend fun writeInitialSubCategory(user: User)

    suspend fun getTopSubCategories(
        uid: String, onCancelled: (errorCode: Int, message: String) -> Unit
    ): StateFlow<List<SubCategory>>

    suspend fun getBottomSubCategories(
        uid: String, onCancelled: (errorCode: Int, message: String) -> Unit
    ): StateFlow<List<SubCategory>>

    suspend fun getOuterSubCategories(
        uid: String, onCancelled: (errorCode: Int, message: String) -> Unit
    ): StateFlow<List<SubCategory>>

    suspend fun getEtcSubCategories(
        uid: String, onCancelled: (errorCode: Int, message: String) -> Unit
    ): StateFlow<List<SubCategory>>

    suspend fun addSubCategory(
        uid: String,
        subCategoryParent: SubCategoryParent,
        name: String,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    )
}
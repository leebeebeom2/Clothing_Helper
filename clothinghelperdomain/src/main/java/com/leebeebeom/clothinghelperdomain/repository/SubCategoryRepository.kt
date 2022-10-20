package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    suspend fun writeInitialSubCategory(user: User)

    fun getTopSubCategories(
        uid: String, onCancelled: (errorCode: Int, message: String) -> Unit
    ): StateFlow<List<SubCategory>>

    fun getBottomSubCategories(
        uid: String,
        onCancelled: (Int, String) -> Unit
    ): StateFlow<List<SubCategory>>

    fun getOuterSubCategories(
        uid: String,
        onCancelled: (Int, String) -> Unit
    ): StateFlow<List<SubCategory>>

    fun getEtcSubCategories(
        uid: String,
        onCancelled: (Int, String) -> Unit
    ): StateFlow<List<SubCategory>>

    suspend fun addSubCategory(subCategoryParent: SubCategoryParent, name: String, uid: String)
}
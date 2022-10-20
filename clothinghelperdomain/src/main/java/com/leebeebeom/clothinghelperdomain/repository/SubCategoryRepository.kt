package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    suspend fun writeInitialSubCategory()

    fun getTopSubCategories(
        onCancelled: (errorCode: Int, message: String) -> Unit
    ): StateFlow<List<SubCategory>>

    fun getBottomSubCategories(
        onCancelled: (Int, String) -> Unit
    ): StateFlow<List<SubCategory>>

    fun getOuterSubCategories(
        onCancelled: (Int, String) -> Unit
    ): StateFlow<List<SubCategory>>

    fun getEtcSubCategories(
        onCancelled: (Int, String) -> Unit
    ): StateFlow<List<SubCategory>>

    suspend fun addSubCategory(
        subCategoryParent: SubCategoryParent,
        name: String,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    )
}
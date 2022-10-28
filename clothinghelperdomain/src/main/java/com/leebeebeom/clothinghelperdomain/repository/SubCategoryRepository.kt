package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import jdk.internal.loader.AbstractClassLoaderValue.Sub
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    suspend fun loadSubCategories(
        onSubCategoriesLoadingDone: () -> Unit,
        onSubCategoriesLoadingCancelled: (errorCode: Int, message: String) -> Unit
    )

    fun pushInitialSubCategories(uid: String)

    val allSubCategories: List<StateFlow<List<SubCategory>>>

    fun addSubCategory(
        subCategoryParent: SubCategoryParent,
        name: String,
        taskFailed:(Exception?)->Unit
    )
}
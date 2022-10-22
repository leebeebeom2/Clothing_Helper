package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    suspend fun loadSubCategories(
        onSubCategoriesLoadingDone: List<() -> Unit>,
        onSubCategoriesLoadingCancelled: List<(errorCode: Int, message: String) -> Unit>
    )

    fun pushInitialSubCategories(uid: String)

    val topSubCategories: StateFlow<List<SubCategory>>
    val bottomSubCategories: StateFlow<List<SubCategory>>
    val outerSubCategories: StateFlow<List<SubCategory>>
    val etcSubCategories: StateFlow<List<SubCategory>>

    fun addSubCategory(
        subCategoryParent: SubCategoryParent,
        name: String,
        addSubCategoryListener: FirebaseListener
    )
}
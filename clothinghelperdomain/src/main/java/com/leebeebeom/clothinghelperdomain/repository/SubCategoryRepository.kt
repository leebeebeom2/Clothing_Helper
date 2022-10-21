package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    suspend fun loadSubCategories(
        onDone: List<() -> Unit>,
        onCancelled: List<(Int, String) -> Unit>
    )

    fun writeInitialSubCategory(uid: String)

    val topSubCategories: StateFlow<List<SubCategory>>
    val bottomSubCategories: StateFlow<List<SubCategory>>
    val outerSubCategories: StateFlow<List<SubCategory>>
    val etcSubCategories: StateFlow<List<SubCategory>>

    fun addSubCategory(
        uid: String,
        subCategoryParent: SubCategoryParent,
        name: String,
        addSubCategoryListener: FirebaseListener
    )
}
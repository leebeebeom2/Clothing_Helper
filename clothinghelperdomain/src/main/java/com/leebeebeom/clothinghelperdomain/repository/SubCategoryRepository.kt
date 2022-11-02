package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    val isLoading: StateFlow<Boolean>

    suspend fun loadSubCategories(
        user: User?,
        onFailed: (Exception) -> Unit
    )

    fun pushInitialSubCategories(uid: String)

    suspend fun getAllSubCategories(
        scope: CoroutineScope,
        sortPreferencesFlow: Flow<SubCategorySortPreferences>
    ): StateFlow<List<SubCategory>>

    fun addSubCategory(
        subCategoryParent: SubCategoryParent,
        name: String,
        uid: String,
        taskFailed: (Exception?) -> Unit
    )

    fun editSubCategoryName(
        subCategory: SubCategory,
        newName: String,
        uid: String,
        taskFailed: (Exception?) -> Unit
    )
}
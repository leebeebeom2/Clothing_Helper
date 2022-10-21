package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.coroutines.flow.StateFlow

interface SubCategoryRepository {
    fun writeInitialSubCategory(uid: String)

    fun getTopSubCategories(
        uid: String, onCancelled: (errorCode: Int, message: String) -> Unit
    ): StateFlow<List<SubCategory>>

    fun getBottomSubCategories(
        uid: String, onCancelled: (errorCode: Int, message: String) -> Unit
    ): StateFlow<List<SubCategory>>

    fun getOuterSubCategories(
        uid: String, onCancelled: (errorCode: Int, message: String) -> Unit
    ): StateFlow<List<SubCategory>>

    fun getEtcSubCategories(
        uid: String, onCancelled: (errorCode: Int, message: String) -> Unit
    ): StateFlow<List<SubCategory>>

    fun addSubCategory(
        uid: String,
        subCategoryParent: SubCategoryParent,
        name: String,
        addSubCategoryListener: FirebaseListener
    )
}
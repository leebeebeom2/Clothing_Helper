package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.container.SubCategory
import kotlinx.coroutines.flow.Flow

interface SubCategoryRepository :ContainerRepository<SubCategory> {
    val allSubCategories: Flow<List<SubCategory>>

    suspend fun pushInitialSubCategories(uid: String)
}
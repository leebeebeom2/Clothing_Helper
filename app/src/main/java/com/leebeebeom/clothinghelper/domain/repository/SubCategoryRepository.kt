package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory

interface SubCategoryRepository : BaseDataRepository<SubCategory> {
    suspend fun pushInitialSubCategories(uid: String) : FirebaseResult
}
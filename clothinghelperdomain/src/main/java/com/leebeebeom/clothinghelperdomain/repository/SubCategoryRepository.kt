package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.data.SubCategory

interface SubCategoryRepository : BaseDataRepository<SubCategory>, LoadingRepository {
    suspend fun pushInitialSubCategories(uid: String)
}
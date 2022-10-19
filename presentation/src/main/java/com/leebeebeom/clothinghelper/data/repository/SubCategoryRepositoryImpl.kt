package com.leebeebeom.clothinghelper.data.repository

import com.leebeebeom.clothinghelper.data.datasource.SubCategoryRemoteDataSource
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository

class SubCategoryRepositoryImpl(
    private val subCategoryRemoteDataSource: SubCategoryRemoteDataSource
) : SubCategoryRepository {
    override fun writeInitialSubCategory() = subCategoryRemoteDataSource.writeInitialData()
}
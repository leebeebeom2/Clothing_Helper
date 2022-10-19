package com.leebeebeom.clothinghelper.data.repository

import com.leebeebeom.clothinghelper.data.datasource.SubCategoryRemoteDataSource
import com.leebeebeom.clothinghelper.data.model.SubCategory
import com.leebeebeom.clothinghelper.data.model.SubCategoryParent
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import kotlinx.coroutines.flow.StateFlow

class SubCategoryRepositoryImpl(
    private val subCategoryRemoteDataSource: SubCategoryRemoteDataSource
) : SubCategoryRepository {
    override val allSubCategories: StateFlow<List<SubCategory>>
        get() = subCategoryRemoteDataSource.allSubCategories
    override val topSubCategories: StateFlow<List<SubCategory>>
        get() = subCategoryRemoteDataSource.topSubCategories
    override val bottomSubCategories: StateFlow<List<SubCategory>>
        get() = subCategoryRemoteDataSource.bottomSubCategories
    override val outerSubCategories: StateFlow<List<SubCategory>>
        get() = subCategoryRemoteDataSource.outerSubCategories
    override val etcSubCategories: StateFlow<List<SubCategory>>
        get() = subCategoryRemoteDataSource.etcSubCategories

    override fun writeInitialSubCategory() = subCategoryRemoteDataSource.writeInitialData()

    override fun addSubCategory(subCategoryParent: SubCategoryParent, name: String) =
        subCategoryRemoteDataSource.addSubCategory(subCategoryParent, name)
}
package com.leebeebeom.clothinghelperdata.repository

import com.leebeebeom.clothinghelperdata.datasource.SubCategoryRemoteDataSource
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import kotlinx.coroutines.flow.StateFlow

class SubCategoryRepositoryImpl(
    private val subCategoryRemoteDataSource: SubCategoryRemoteDataSource
) : com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository {
    override val allSubCategories: StateFlow<List<com.leebeebeom.clothinghelperdomain.model.SubCategory>>
        get() = subCategoryRemoteDataSource.allSubCategories
    override val topSubCategories: StateFlow<List<com.leebeebeom.clothinghelperdomain.model.SubCategory>>
        get() = subCategoryRemoteDataSource.topSubCategories
    override val bottomSubCategories: StateFlow<List<com.leebeebeom.clothinghelperdomain.model.SubCategory>>
        get() = subCategoryRemoteDataSource.bottomSubCategories
    override val outerSubCategories: StateFlow<List<com.leebeebeom.clothinghelperdomain.model.SubCategory>>
        get() = subCategoryRemoteDataSource.outerSubCategories
    override val etcSubCategories: StateFlow<List<com.leebeebeom.clothinghelperdomain.model.SubCategory>>
        get() = subCategoryRemoteDataSource.etcSubCategories

    override fun writeInitialSubCategory() = subCategoryRemoteDataSource.writeInitialData()

    override fun addSubCategory(subCategoryParent: com.leebeebeom.clothinghelperdomain.model.SubCategoryParent, name: String) =
        subCategoryRemoteDataSource.addSubCategory(subCategoryParent, name)
}
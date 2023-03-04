package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.usecase.BaseGetAllDataUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllSubCategoriesUseCase @Inject constructor(subCategoryRepository: SubCategoryRepository) :
    BaseGetAllDataUseCase<SubCategory>(repository = subCategoryRepository)
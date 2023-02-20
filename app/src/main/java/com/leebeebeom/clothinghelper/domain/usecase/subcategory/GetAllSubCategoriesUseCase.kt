package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetAllSubCategoriesUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    val allSubCategories get() = subCategoryRepository.allData
}
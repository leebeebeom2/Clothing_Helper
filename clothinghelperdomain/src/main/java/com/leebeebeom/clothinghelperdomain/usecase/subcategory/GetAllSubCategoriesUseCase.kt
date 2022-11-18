package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetAllSubCategoriesUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    val allSubCategories get() = subCategoryRepository.allSubCategories
}
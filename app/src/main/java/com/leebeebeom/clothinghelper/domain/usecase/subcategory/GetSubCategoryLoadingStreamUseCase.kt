package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import javax.inject.Inject

class GetSubCategoryLoadingStreamUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    val subCategoryLoadingStream get() = subCategoryRepository.loadingStream
}
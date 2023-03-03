package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.usecase.BaseGetIsDataLoadingStateUseCase
import javax.inject.Inject

class GetIsSubCategoryLoadingStateUserCase @Inject constructor(
    subCategoryRepository: SubCategoryRepository,
) : BaseGetIsDataLoadingStateUseCase(repository = subCategoryRepository)
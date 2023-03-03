package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.usecase.BaseGetIsDataLoadingStateUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GetIsSubCategoryLoadingStateUserCase @Inject constructor(
    subCategoryRepository: SubCategoryRepository,
    @AppScope appScope: CoroutineScope
) : BaseGetIsDataLoadingStateUseCase(repository = subCategoryRepository, appScope = appScope)
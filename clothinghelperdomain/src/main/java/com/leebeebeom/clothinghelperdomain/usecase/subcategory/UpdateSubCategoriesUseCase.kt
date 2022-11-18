package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@ViewModelScoped
class UpdateSubCategoriesUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val subCategoryRepository: SubCategoryRepository
) {
    suspend fun update(onUpdateSubCategoriesFail: (FirebaseResult) -> Unit) =
        getUserUseCase.user.collectLatest {
            it?.let { onUpdateSubCategoriesFail(subCategoryRepository.loadSubCategories(it.uid)) }
        }
}
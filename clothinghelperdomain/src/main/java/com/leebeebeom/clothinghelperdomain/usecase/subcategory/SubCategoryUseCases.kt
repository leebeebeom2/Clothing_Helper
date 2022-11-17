package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.*
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategorySortUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@ViewModelScoped
class GetSubCategoryLoadingStateUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(): StateFlow<Boolean> {
        return subCategoryRepository.isLoading
    }
}

@ViewModelScoped
class PushInitialSubCategoriesUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(uid: String, onUpdateSubCategoriesFail: (FirebaseResult) -> Unit) {
        subCategoryRepository.pushInitialSubCategories(uid)
        onUpdateSubCategoriesFail(subCategoryRepository.updateSubCategories(uid))
    }
}

@ViewModelScoped
class GetAllSubCategoriesUseCase @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository,
    private val subCategorySortUseCase: SubCategorySortUseCase
) {
    operator fun invoke(): Flow<List<List<SubCategory>>> {
        return combine(
            subCategoryRepository.allSubCategories,
            subCategorySortUseCase.sortPreferences
        ) { allSubCategories, sort ->
            val temp = allSubCategories.toMutableList().map { it.toMutableList() }
            temp.map { getSortSubCategories(it, sort) }
        }
    }
}

private fun getSortSubCategories(
    subCategories: List<SubCategory>, sortPreferences: SubCategorySortPreferences
): List<SubCategory> {
    val sort = sortPreferences.sort
    val order = sortPreferences.sortOrder

    return when {
        sort == SubCategorySort.NAME && order == SortOrder.ASCENDING -> subCategories.sortedBy { it.name }
        sort == SubCategorySort.NAME && order == SortOrder.DESCENDING -> subCategories.sortedByDescending { it.name }
        sort == SubCategorySort.CREATE && order == SortOrder.ASCENDING -> subCategories.sortedBy { it.createDate }
        sort == SubCategorySort.CREATE && order == SortOrder.DESCENDING -> subCategories.sortedByDescending { it.createDate }
        else -> subCategories
    }
}

@ViewModelScoped
class UpdateSubCategoriesUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val subCategoryRepository: SubCategoryRepository
) {
    suspend operator fun invoke(onUpdateSubCategoriesFail: (FirebaseResult) -> Unit) {
        getUserUseCase().collectLatest {
            it?.let { user ->
                onUpdateSubCategoriesFail(subCategoryRepository.updateSubCategories(user.uid))
            }
        }
    }
}

@ViewModelScoped
class AddSubCategoryUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(subCategory: SubCategory): FirebaseResult {
        return subCategoryRepository.addSubCategory(subCategory)
    }
}

@ViewModelScoped
class EditSubCategoryNameUseCase @Inject constructor(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(newSubCategory: SubCategory): FirebaseResult {
        return subCategoryRepository.editSubCategoryName(newSubCategory)
    }
}
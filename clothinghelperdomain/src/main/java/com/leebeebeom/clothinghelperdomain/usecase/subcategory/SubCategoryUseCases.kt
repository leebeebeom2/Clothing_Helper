package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategorySortUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

class GetSubCategoryLoadingStateUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(): StateFlow<Boolean> {
        return subCategoryRepository.isLoading
    }
}

class PushInitialSubCategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(uid: String, onUpdateSubCategoriesFail: (FirebaseResult) -> Unit) {
        subCategoryRepository.pushInitialSubCategories(uid)
        onUpdateSubCategoriesFail(subCategoryRepository.updateSubCategories(uid))
    }
}

class GetAllSubCategoriesUseCase(
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
    subCategories: List<SubCategory>, preferences: SubCategorySortPreferences
): List<SubCategory> {
    val sort = preferences.sort
    val order = preferences.sortOrder

    return when {
        sort == SubCategorySort.NAME && order == SortOrder.ASCENDING -> subCategories.sortedBy { it.name }
        sort == SubCategorySort.NAME && order == SortOrder.DESCENDING -> subCategories.sortedByDescending { it.name }
        sort == SubCategorySort.CREATE && order == SortOrder.ASCENDING -> subCategories.sortedBy { it.createDate }
        sort == SubCategorySort.CREATE && order == SortOrder.DESCENDING -> subCategories.sortedByDescending { it.createDate }
        else -> subCategories
    }
}

class UpdateSubCategoriesUseCase(
    private val getUserUseCase: GetUserUseCase,
    private val subCategoryRepository: SubCategoryRepository
) {
    operator fun invoke(): Flow<FirebaseResult> {
        return combine(getUserUseCase()) {
            it[0]?.let { user ->
                subCategoryRepository.updateSubCategories(user.uid)
            } ?: FirebaseResult.Fail(NullPointerException("user is null"))
        }
    }
}

class AddSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(subCategory: SubCategory): FirebaseResult {
        return subCategoryRepository.addSubCategory(subCategory)
    }
}

class EditSubCategoryNameUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(newSubCategory: SubCategory): FirebaseResult {
        return subCategoryRepository.editSubCategoryName(newSubCategory)
    }
}
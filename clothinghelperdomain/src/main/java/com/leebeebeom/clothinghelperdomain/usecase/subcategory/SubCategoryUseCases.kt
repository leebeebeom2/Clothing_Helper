package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.*
import com.leebeebeom.clothinghelperdomain.repository.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

class GetSubCategoryLoadingStateUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(): StateFlow<Boolean> {
        return subCategoryRepository.isLoading
    }
}

class PushInitialSubCategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(uid: String): SubCategoryPushResult {
        return subCategoryRepository.pushInitialSubCategories(uid)
    }
}

class LoadSubCategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(user: User?): FirebaseResult {
        return subCategoryRepository.loadSubCategories(user)
    }
}

class GetSubCategoriesUseCase(
    private val subCategoryRepository: SubCategoryRepository,
    private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository
) {
    operator fun invoke() =
        subCategoryRepository.allSubCategories.combine(subCategoryPreferencesRepository.sort) { allSubCategories, sort ->
            val temp = arrayListOf<List<SubCategory>>()
            allSubCategories.forEach {
                temp.add(getSortSubCategories(subCategories = it, preferences = sort))
            }
            temp.toList()
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

class AddSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(
        subCategoryParent: SubCategoryParent,
        name: String,
        uid: String
    ): FirebaseResult {
        return subCategoryRepository.addSubCategory(
            subCategoryParent = subCategoryParent,
            name = name,
            uid = uid
        )
    }
}

class EditSubCategoryNameUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(newSubCategory: SubCategory, uid: String): FirebaseResult {
        return subCategoryRepository.editSubCategoryName(newSubCategory, uid)
    }
}
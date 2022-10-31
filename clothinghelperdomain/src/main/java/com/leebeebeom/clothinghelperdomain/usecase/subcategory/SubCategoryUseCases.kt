package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetUserUseCase
import kotlinx.coroutines.CoroutineScope

class GetSubCategoryLoadingStateUseCase(private val subCategoryRepository: SubCategoryRepository) {
    val isLoading get() = subCategoryRepository.isLoading
}

class LoadSubCategoriesUseCase(
    private val subCategoryRepository: SubCategoryRepository,
    private val getUserUseCase: GetUserUseCase
) {
    suspend operator fun invoke(onFailed: (Exception) -> Unit) {
        getUserUseCase().collect { subCategoryRepository.loadSubCategories(it, onFailed) }
    }
}

class GetSubCategoriesUseCase(
    private val subCategoryRepository: SubCategoryRepository,
    private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository
) {
    suspend operator fun invoke(scope: CoroutineScope) = subCategoryRepository.getAllSubCategories(
        scope, subCategoryPreferencesRepository.subCategorySort
    )
}

class AddSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(
        subCategoryParent: SubCategoryParent,
        name: String,
        uid: String,
        taskFailed: (Exception?) -> Unit
    ) = subCategoryRepository.addSubCategory(
        subCategoryParent = subCategoryParent, name = name, uid = uid, taskFailed = taskFailed
    )
}

class EditSubCategoryNameUseCase(private val subCategoryRepository: SubCategoryRepository) {
    operator fun invoke(
        subCategory: SubCategory, newName: String, uid: String, taskFailed: (Exception?) -> Unit
    ) = subCategoryRepository.editSubCategoryName(subCategory, newName, uid, taskFailed)
}

class ChangeSortUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    suspend operator fun invoke(subCategorySort: SubCategorySort) =
        subCategoryPreferencesRepository.changeSort(subCategorySort)
}

class ChangeOrderUserCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    suspend operator fun invoke(sortOrder: SortOrder) =
        subCategoryPreferencesRepository.changeOrder(sortOrder)
}
package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.model.container.SubCategory
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategorySortUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@ViewModelScoped
class GetAllSubCategoriesUseCase @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository,
    private val subCategorySortUseCase: SubCategorySortUseCase
) {
    val allSubCategories
        get() = combine(
            subCategoryRepository.allSubCategories, subCategorySortUseCase.sortPreferences
        ) { allSubCategories, sort ->
            getSortSubCategories(allSubCategories, sort)
        }
}

private fun getSortSubCategories(
    subCategories: List<SubCategory>, sortPreferences: SortPreferences
): List<SubCategory> {
    val sort = sortPreferences.sort
    val order = sortPreferences.order

    return when {
        sort == Sort.NAME && order == Order.ASCENDING -> subCategories.sortedBy { it.name }
        sort == Sort.NAME && order == Order.DESCENDING -> subCategories.sortedByDescending { it.name }
        sort == Sort.CREATE && order == Order.ASCENDING -> subCategories.sortedBy { it.createDate }
        sort == Sort.CREATE && order == Order.DESCENDING -> subCategories.sortedByDescending { it.createDate }
        sort == Sort.EDIT && order == Order.ASCENDING -> subCategories.sortedBy { it.editDate }
        sort == Sort.EDIT && order == Order.DESCENDING -> subCategories.sortedByDescending { it.editDate }
        else -> subCategories
    }
}
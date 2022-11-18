package com.leebeebeom.clothinghelper.main.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.BaseMainUIState
import com.leebeebeom.clothinghelper.main.subcategory.AddSubCategory
import com.leebeebeom.clothinghelper.main.subcategory.EditSubCategoryName
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.*
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainRootViewModel @Inject constructor(
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    private val loadSubCategoriesUseCase: LoadSubCategoriesUseCase,
    private val getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase,
    private val getUserUseCase: GetUserUseCase,
    override val addSubCategoryUseCase: AddSubCategoryUseCase,
    override val editSubCategoryNameUseCase: EditSubCategoryNameUseCase
) : AddSubCategory, EditSubCategoryName, ViewModel() {

    val uiState = BaseMainUIState()

    init {
        viewModelScope.launch {
            launch {
                loadSubCategoriesUseCase.load {
                    if (it is FirebaseResult.Fail)
                        when (it.exception) {
                            is TimeoutCancellationException -> showToast(R.string.network_error_for_load)
                            !is NullPointerException -> uiState.showToast(R.string.sub_categories_load_failed)
                        }
                }
            }

            launch { getSubCategoryLoadingStateUseCase.isLoading.collectLatest(uiState::updateIsLoading) }
            launch { getAllSubCategoriesUseCase.allSubCategories.collectLatest(uiState::loadAllSubCategories) }
            launch { getUserUseCase.user.collectLatest(uiState::updateUser) }
        }
    }

    fun addSubCategory(subCategory:StableSubCategory){
        viewModelScope.launch {
            super.add(subCategory)
        }
    }

    fun editSubCategoryName(subCategory: StableSubCategory){
        viewModelScope.launch {
            super.edit(subCategory)
        }
    }

    override fun showToast(text: Int) = uiState.showToast(text)
    override val uid get() = uiState.user?.uid
}
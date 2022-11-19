package com.leebeebeom.clothinghelper.main.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.ToastUIState
import com.leebeebeom.clothinghelper.base.ToastUIStateImpl
import com.leebeebeom.clothinghelper.main.base.*
import com.leebeebeom.clothinghelper.main.subcategory.interfaces.AddSubCategory
import com.leebeebeom.clothinghelper.main.subcategory.interfaces.EditSubCategoryName
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

    val uiState = MainRootUIState()

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

    fun addSubCategory(subCategory: StableSubCategory) {
        viewModelScope.launch {
            super.baseAddSubCategory(subCategory)
        }
    }

    fun editSubCategoryName(subCategory: StableSubCategory) {
        viewModelScope.launch {
            super.baseEditSubCategoryName(subCategory)
        }
    }

    override fun showToast(text: Int) = uiState.showToast(text)
    override val uid get() = uiState.user?.uid
}

class MainRootUIState :
    ToastUIState by ToastUIStateImpl(),
    UserUIState by UserUIStateImpl(),
    LoadingUIState by LoadingUIStateImpl(),
    SubCategoryUIState by SubCategoryUIStateImpl()
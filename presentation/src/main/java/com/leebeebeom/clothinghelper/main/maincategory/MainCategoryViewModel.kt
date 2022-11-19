package com.leebeebeom.clothinghelper.main.maincategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.main.base.LoadingUIState
import com.leebeebeom.clothinghelper.main.base.LoadingUIStateImpl
import com.leebeebeom.clothinghelper.main.base.SubCategoryUIState
import com.leebeebeom.clothinghelper.main.base.SubCategoryUIStateImpl
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetAllSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase,
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase
) : ViewModel() {

    val uiState = MainCategoryUIState()

    init {
        viewModelScope.launch {
            launch { getSubCategoryLoadingStateUseCase.isLoading.collectLatest(uiState::updateIsLoading) }
            launch { getAllSubCategoriesUseCase.allSubCategories.collectLatest(uiState::loadAllSubCategories) }
        }
    }
}

class MainCategoryUIState :
    LoadingUIState by LoadingUIStateImpl(),
    SubCategoryUIState by SubCategoryUIStateImpl()
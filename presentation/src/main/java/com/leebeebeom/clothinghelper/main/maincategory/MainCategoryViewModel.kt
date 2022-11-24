package com.leebeebeom.clothinghelper.main.maincategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.main.base.interfaces.LoadingUIState
import com.leebeebeom.clothinghelper.main.base.interfaces.LoadingUIStateImpl
import com.leebeebeom.clothinghelper.main.base.interfaces.SubCategoryUIState
import com.leebeebeom.clothinghelper.main.base.interfaces.SubCategoryUIStateImpl
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetAllSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.GetDataLoadingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase,
    private val getDataLoadingStateUseCase: GetDataLoadingStateUseCase
) : ViewModel() {

    val uiState = MainCategoryUIState()

    init {
        viewModelScope.launch {
            launch { getDataLoadingStateUseCase.isLoading.collectLatest(uiState::updateIsLoading) }
            launch { getAllSubCategoriesUseCase.allSubCategories.collectLatest(uiState::loadAllSubCategories) }
        }
    }
}

class MainCategoryUIState :
    LoadingUIState by LoadingUIStateImpl(),
    SubCategoryUIState by SubCategoryUIStateImpl()
package com.leebeebeom.clothinghelper.main.maincategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.main.base.BaseMainUIState
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

    val uiStates = BaseMainUIState()

    init {
        viewModelScope.launch {
            launch { getSubCategoryLoadingStateUseCase().collectLatest(uiStates::updateIsLoading) }
            launch { getAllSubCategoriesUseCase().collectLatest(uiStates::updateAllSubCategories) }
        }
    }
}
package com.leebeebeom.clothinghelper.main.maincategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.main.base.BaseMainUIState
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase
) : ViewModel() {

    val uiStates = BaseMainUIState()

    init {
        viewModelScope.launch {
            getSubCategoryLoadingStateUseCase().collect(uiStates::updateIsLoading)
        }

        viewModelScope.launch {
            getSubCategoriesUseCase().collect(uiStates::updateAllSubCategories)
        }
    }
}
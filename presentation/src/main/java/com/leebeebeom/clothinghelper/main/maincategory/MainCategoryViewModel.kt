package com.leebeebeom.clothinghelper.main.maincategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.main.base.BaseSubCategoryUIState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainCategoryUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getSubCategoryLoadingStateUseCase(),
                getSubCategoriesUseCase()
            ) { isLoading, allSubCategories ->
                MainCategoryUIState(isLoading = isLoading, allSubCategories = allSubCategories)
            }.collect {
                _uiState.value = it
            }
        }
    }
}

data class MainCategoryUIState(
    override val toastText: Int? = null,
    override val user: User? = null,
    override val allSubCategories: List<List<SubCategory>> = List(4) { emptyList() },
    override val isLoading: Boolean = false
) : BaseSubCategoryUIState()
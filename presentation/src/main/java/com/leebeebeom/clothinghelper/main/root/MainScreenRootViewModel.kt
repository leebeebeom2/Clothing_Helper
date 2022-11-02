package com.leebeebeom.clothinghelper.main.root

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.main.base.BaseSubCategoryUIState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.usecase.preferences.MainScreenRootAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetUserUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenRootViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val loadSubCategoriesUseCase: LoadSubCategoriesUseCase,
    private val mainScreenRootAllExpandUseCase: MainScreenRootAllExpandUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainRootUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { loadSubCategoriesUseCase(::onLoadFailed) }

        viewModelScope.launch {
            combine(
                getUserUseCase(),
                getSubCategoryLoadingStateUseCase(),
                getSubCategoriesUseCase(),
                mainScreenRootAllExpandUseCase.isAllExpand
            ) { user, isLoading, allSubCategories, isAllExpand ->
                MainRootUIState(
                    user = user,
                    isLoading = isLoading,
                    allSubCategories = allSubCategories,
                    isAllExpand = isAllExpand
                )
            }.collect { _uiState.value = it }
        }
    }

    fun toggleAllExpand() =
        viewModelScope.launch {
            mainScreenRootAllExpandUseCase.toggleAllExpand()
        }

    private fun onLoadFailed(exception: Exception) {
        _uiState.update { it.copy(toastText = R.string.sub_categories_load_failed) }
        Log.e(TAG, "onLoadFailed: ${exception.message}", exception)
    }
}

data class MainRootUIState(
    override val toastText: Int? = null,
    override val user: User? = null,
    override val isLoading: Boolean = false,
    override val allSubCategories: List<List<SubCategory>> = List(4) { emptyList() },
    val isAllExpand: Boolean = false
) : BaseSubCategoryUIState()
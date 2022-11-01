package com.leebeebeom.clothinghelper.main.root

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.main.base.BaseSubCategoriesViewModel
import com.leebeebeom.clothinghelper.main.base.BaseSubCategoriesViewModelState
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetUserUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenRootViewModel @Inject constructor(
    getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    getSubCategoriesUseCase: GetSubCategoriesUseCase,
    getUserUseCase: GetUserUseCase,
    private val loadSubCategoriesUseCase: LoadSubCategoriesUseCase
) : BaseSubCategoriesViewModel(
    getUserUseCase = getUserUseCase,
    getSubCategoryLoadingStateUseCase = getSubCategoryLoadingStateUseCase,
    getSubCategoriesUseCase = getSubCategoriesUseCase
) {
    override val viewModelState = MainRootViewModelState()

    init {
        collectUser()
        collectIsLoading()

        viewModelScope.launch { loadSubCategoriesUseCase(viewModelState::onLoadFailed) }

        collectSubCategories()
    }
}

class MainRootViewModelState : BaseSubCategoriesViewModelState() {
    fun onLoadFailed(exception: Exception?) {
        showToast(R.string.sub_categories_load_failed)
        Log.d(
            TAG, "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $exception"
        )
    }
}
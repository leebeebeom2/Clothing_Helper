package com.leebeebeom.clothinghelper.main.root

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.main.base.BaseIsAllExpandState
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.usecase.preferences.MainScreenRootAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetUserUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainRootViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val loadSubCategoriesUseCase: LoadSubCategoriesUseCase,
    private val mainScreenRootAllExpandUseCase: MainScreenRootAllExpandUseCase
) : ViewModel() {

    val uiStates = BaseIsAllExpandState()

    init {
        viewModelScope.launch {
            getUserUseCase().collect {
                uiStates.updateUser(it)
                when (val result = loadSubCategoriesUseCase(it)) {
                    is FirebaseResult.Success -> {}
                    is FirebaseResult.Fail -> {
                        uiStates.showToast(R.string.sub_categories_load_failed)
                        Log.d(TAG, "subCategoryLoadFail: $result")
                    }
                }

            }
        }

        viewModelScope.launch {
            getSubCategoryLoadingStateUseCase().collect(uiStates::updateIsLoading)
        }

        viewModelScope.launch {
            getSubCategoriesUseCase().collect(uiStates::updateAllSubCategories)
        }

        viewModelScope.launch {
            mainScreenRootAllExpandUseCase.isAllExpand.collect(uiStates::updateIsAllExpand)
        }
    }

    fun toggleAllExpand() {
        viewModelScope.launch {
            mainScreenRootAllExpandUseCase.toggleAllExpand()
        }
    }
}
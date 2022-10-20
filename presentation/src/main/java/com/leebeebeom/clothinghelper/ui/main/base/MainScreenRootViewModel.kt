package com.leebeebeom.clothinghelper.ui.main.base

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.TAG
import com.leebeebeom.clothinghelperdomain.model.*
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUserCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenRootViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUserCase
) : ViewModel() {
    val viewModelState = MainNavHostViewModelState()

    init {
        viewModelScope.launch {
            getUserUseCase.getUser().collect(viewModelState::userUpdate)
        }
        viewModelScope.launch {
            getSubCategoriesUseCase.getTopSubCategories(viewModelState::subCategoryLoadFailed)
                .collect(viewModelState::topSubCategoriesUpdate)
        }
        viewModelScope.launch {
            getSubCategoriesUseCase.getBottomSubCategories(viewModelState::subCategoryLoadFailed)
                .collect(viewModelState::bottomSubCategoriesUpdate)
        }
        viewModelScope.launch {
            getSubCategoriesUseCase.getOuterSubCategories(viewModelState::subCategoryLoadFailed)
                .collect(viewModelState::outerSubCategoriesUpdate)
        }
        viewModelScope.launch {
            getSubCategoriesUseCase.getEtcSubCategories(viewModelState::subCategoryLoadFailed)
                .collect(viewModelState::etcSubCategoriesUpdate)
        }
    }
}

class MainNavHostViewModelState {
    var user by mutableStateOf(User())
        private set

    fun userUpdate(user: User) {
        this.user = user
    }

    var toastText: Int? by mutableStateOf(null)

    fun subCategoryLoadFailed(errorCode: Int, message: String) {
        showToast(R.string.data_load_failed)
        Log.d(
            TAG,
            "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $errorCode, $message"
        )
    }

    private fun showToast(@StringRes toastText: Int) {
        this.toastText = toastText
    }

    fun toastShown() {
        this.toastText = null
    }

    val mainCategories = listOf(
        MainCategory(BaseMenuIds.TOP, R.string.top, SubCategoryParent.Top),
        MainCategory(BaseMenuIds.BOTTOM, R.string.bottom, SubCategoryParent.Bottom),
        MainCategory(BaseMenuIds.OUTER, R.string.outer, SubCategoryParent.OUTER),
        MainCategory(BaseMenuIds.ETC, R.string.etc, SubCategoryParent.ETC),
    )

    var topSubCategories by mutableStateOf(emptyList<SubCategory>())
        private set
    var bottomSubCategories by mutableStateOf(emptyList<SubCategory>())
        private set
    var outerSubCategories by mutableStateOf(emptyList<SubCategory>())
        private set
    var etcSubCategories by mutableStateOf(emptyList<SubCategory>())
        private set

    fun topSubCategoriesUpdate(topSubCategories: List<SubCategory>) {
        this.topSubCategories = topSubCategories
    }

    fun bottomSubCategoriesUpdate(bottomSubCategories: List<SubCategory>) {
        this.bottomSubCategories = bottomSubCategories
    }

    fun outerSubCategoriesUpdate(outerSubCategories: List<SubCategory>) {
        this.outerSubCategories = outerSubCategories
    }

    fun etcSubCategoriesUpdate(etcSubCategories: List<SubCategory>) {
        this.etcSubCategories = etcSubCategories
    }
}
package com.leebeebeom.clothinghelper.main.base

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.base.BaseViewModelState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User
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
            getUserUseCase().collect(viewModelState::userUpdate)
        }
        viewModelScope.launch {
            getSubCategoriesUseCase.loadSubCategories(
                viewModelState.onDone,
                viewModelState.onCancelled
            )
        }

        viewModelScope.launch {
            getSubCategoriesUseCase.getTopSubCategories()
                .collect(viewModelState::topSubCategoriesUpdate)
        }
        viewModelScope.launch {
            getSubCategoriesUseCase.getBottomSubCategories()
                .collect(viewModelState::bottomSubCategoriesUpdate)
        }
        viewModelScope.launch {
            getSubCategoriesUseCase.getOuterSubCategories()
                .collect(viewModelState::outerSubCategoriesUpdate)
        }
        viewModelScope.launch {
            getSubCategoriesUseCase.getEtcSubCategories()
                .collect(viewModelState::etcSubCategoriesUpdate)
        }
    }
}

class MainNavHostViewModelState : BaseViewModelState() {
    var user by mutableStateOf<User?>(null)
        private set

    var isTopSubCategoriesLoading by mutableStateOf(true)
        private set
    var isBottomSubCategoriesLoading by mutableStateOf(true)
        private set
    var isOuterSubCategoriesLoading by mutableStateOf(true)
        private set
    var isEtcSubCategoriesLoading by mutableStateOf(true)
        private set

    fun userUpdate(user: User?) {
        this.user = user
    }

    fun getSubCategories(subCategoryParent: SubCategoryParent): List<SubCategory> {
        return when (subCategoryParent) {
            SubCategoryParent.Top -> topSubCategories
            SubCategoryParent.Bottom -> bottomSubCategories
            SubCategoryParent.OUTER -> outerSubCategories
            SubCategoryParent.ETC -> etcSubCategories
        }
    }

    fun getIsLoading(subCategoryParent: SubCategoryParent): Boolean {
        return when (subCategoryParent) {
            SubCategoryParent.Top -> isTopSubCategoriesLoading
            SubCategoryParent.Bottom -> isBottomSubCategoriesLoading
            SubCategoryParent.OUTER -> isOuterSubCategoriesLoading
            SubCategoryParent.ETC -> isEtcSubCategoriesLoading
        }
    }

    private var topSubCategories by mutableStateOf(emptyList<SubCategory>())
    private var bottomSubCategories by mutableStateOf(emptyList<SubCategory>())
    private var outerSubCategories by mutableStateOf(emptyList<SubCategory>())
    private var etcSubCategories by mutableStateOf(emptyList<SubCategory>())

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

    val onDone = listOf(
        { isTopSubCategoriesLoading = false },
        { isBottomSubCategoriesLoading = false },
        { isOuterSubCategoriesLoading = false },
        { isEtcSubCategoriesLoading = false }
    )

    val onCancelled = listOf(
        { errorCode: Int, message: String ->
            showToast(R.string.top_sub_categories_load_failed)
            Log.d(
                TAG,
                "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $errorCode, $message"
            )
            Unit
        },
        { errorCode: Int, message: String ->
            showToast(R.string.bottom_sub_categories_load_failed)
            Log.d(
                TAG,
                "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $errorCode, $message"
            )
            Unit
        },
        { errorCode: Int, message: String ->
            showToast(R.string.outer_sub_categories_load_failed)
            Log.d(
                TAG,
                "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $errorCode, $message"
            )
            Unit
        },
        { errorCode: Int, message: String ->
            showToast(R.string.etc_sub_categories_load_failed)
            Log.d(
                TAG,
                "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $errorCode, $message"
            )
            Unit
        },
    )
}
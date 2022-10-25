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
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadAndGetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenRootViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val loadAndGetSubCategoriesUseCase: LoadAndGetSubCategoriesUseCase
) : ViewModel() {
    val viewModelState = MainNavHostViewModelState()

    init {
        viewModelScope.launch {
            getUserUseCase().collect(viewModelState::userUpdate)
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.loadSubCategories(
                viewModelState.onSubCategoriesLoadingDone,
                viewModelState.onSubCategoriesLoadingCancelled
            )
        }

        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.topSubCategories
                .collect(viewModelState::topSubCategoriesUpdate)
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.bottomSubCategories
                .collect(viewModelState::bottomSubCategoriesUpdate)
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.outerSubCategories
                .collect(viewModelState::outerSubCategoriesUpdate)
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.etcSubCategories
                .collect(viewModelState::etcSubCategoriesUpdate)
        }
    }
}

class MainNavHostViewModelState : BaseViewModelState() {
    var user by mutableStateOf<User?>(null)
        private set

    fun userUpdate(user: User?) {
        this.user = user
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

    fun getSubCategories(subCategoryParent: SubCategoryParent): List<SubCategory> {
        return when (subCategoryParent) {
            SubCategoryParent.TOP -> topSubCategories
            SubCategoryParent.BOTTOM -> bottomSubCategories
            SubCategoryParent.OUTER -> outerSubCategories
            SubCategoryParent.ETC -> etcSubCategories
        }
    }

    private var isTopSubCategoriesLoading by mutableStateOf(true)
    private var isBottomSubCategoriesLoading by mutableStateOf(true)
    private var isOuterSubCategoriesLoading by mutableStateOf(true)
    private var isEtcSubCategoriesLoading by mutableStateOf(true)

    fun getIsSubCategoriesLoading(subCategoryParent: SubCategoryParent): Boolean {
        return when (subCategoryParent) {
            SubCategoryParent.TOP -> isTopSubCategoriesLoading
            SubCategoryParent.BOTTOM -> isBottomSubCategoriesLoading
            SubCategoryParent.OUTER -> isOuterSubCategoriesLoading
            SubCategoryParent.ETC -> isEtcSubCategoriesLoading
        }
    }

    val onSubCategoriesLoadingDone = listOf(
        { isTopSubCategoriesLoading = false },
        { isBottomSubCategoriesLoading = false },
        { isOuterSubCategoriesLoading = false },
        { isEtcSubCategoriesLoading = false }
    )

    val onSubCategoriesLoadingCancelled = listOf(
        { errorCode: Int, message: String ->
            showToast(R.string.top_sub_categories_load_failed)
            onSubCategoriesLoadingDone[0]()
            Log.d(
                TAG,
                "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $errorCode, $message"
            )
            Unit
        },
        { errorCode: Int, message: String ->
            showToast(R.string.bottom_sub_categories_load_failed)
            onSubCategoriesLoadingDone[1]()
            Log.d(
                TAG,
                "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $errorCode, $message"
            )
            Unit
        },
        { errorCode: Int, message: String ->
            showToast(R.string.outer_sub_categories_load_failed)
            onSubCategoriesLoadingDone[2]()
            Log.d(
                TAG,
                "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $errorCode, $message"
            )
            Unit
        },
        { errorCode: Int, message: String ->
            showToast(R.string.etc_sub_categories_load_failed)
            onSubCategoriesLoadingDone[3]()
            Log.d(
                TAG,
                "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $errorCode, $message"
            )
            Unit
        },
    )
}
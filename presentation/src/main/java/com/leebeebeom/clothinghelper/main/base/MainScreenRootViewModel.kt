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
            getUserUseCase().collect(viewModelState.userUpdate)
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.loadSubCategories(
                viewModelState.onSubCategoriesLoadingDone,
                viewModelState.onSubCategoriesLoadingCancelled
            )
        }

        loadAndGetSubCategoriesUseCase.allSubCategories.forEachIndexed { i, subCategoriesFlow ->
            viewModelScope.launch {
                subCategoriesFlow.collect {
                    viewModelState.subCategoriesUpdate(i, it)
                }
            }
        }
    }
}

class MainNavHostViewModelState : BaseViewModelState() {
    var user by mutableStateOf<User?>(null)
        private set

    val userUpdate = { user: User? -> this.user = user }

    private val allSubCategories = List(4) { mutableStateOf(emptyList<SubCategory>()) }

    fun subCategoriesUpdate(index: Int, subCategories: List<SubCategory>) {
        allSubCategories[index].value = subCategories
    }

    val getSubCategories =
        { subCategoryParent: SubCategoryParent -> allSubCategories[subCategoryParent.ordinal].value }

    var isSubCategoriesLoading by mutableStateOf(true)
        private set

    val onSubCategoriesLoadingDone = { isSubCategoriesLoading = false }

    val onSubCategoriesLoadingCancelled =
        { errorCode: Int, message: String ->
            showToast(R.string.sub_categories_load_failed)
            onSubCategoriesLoadingDone()
            Log.d(
                TAG,
                "MainScreenRootViewModel.subCategoryLoadFailed: errorCode = $errorCode, $message"
            )
            Unit
        }
}
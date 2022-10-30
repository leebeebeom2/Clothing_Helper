package com.leebeebeom.clothinghelper.main.base

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadAndGetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenRootViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val loadAndGetSubCategoriesUseCase: LoadAndGetSubCategoriesUseCase
) : BaseSubCategoriesViewModel(loadAndGetSubCategoriesUseCase) {
    override val viewModelState = MainNavHostViewModelState()

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

        collectSubCategories()
    }
}

class MainNavHostViewModelState : BaseSubCategoriesViewModelState() {
    var user by mutableStateOf<User?>(null)
        private set

    val userUpdate = { user: User? -> this.user = user }

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
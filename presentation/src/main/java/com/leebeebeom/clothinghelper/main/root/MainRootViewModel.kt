package com.leebeebeom.clothinghelper.main.root

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.BaseMainUIState
import com.leebeebeom.clothinghelper.main.subcategory.EditSubCategoryNameViewModel
import com.leebeebeom.clothinghelper.map.StableUser
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.*
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainRootViewModel @Inject constructor(
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    private val loadSubCategoriesUseCase: LoadSubCategoriesUseCase,
    private val getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase,
    private val getUserUseCase: GetUserUseCase,
    addSubCategoryUseCase: AddSubCategoryUseCase,
    editSubCategoryNameUseCase: EditSubCategoryNameUseCase
) : EditSubCategoryNameViewModel(editSubCategoryNameUseCase, addSubCategoryUseCase) {

    val uiState = MainRootUiState()

    init {
        viewModelScope.launch {
            launch {
                loadSubCategoriesUseCase.load {
                    if (it is FirebaseResult.Fail)
                        when (it.exception) {
                            is TimeoutCancellationException -> showToast(R.string.network_error_for_load)
                            !is NullPointerException -> uiState.showToast(R.string.sub_categories_load_failed)
                        }
                }
            }

            launch { getSubCategoryLoadingStateUseCase.isLoading.collectLatest(uiState::updateIsLoading) }
            launch { getAllSubCategoriesUseCase.allSubCategories.collectLatest(uiState::loadAllSubCategories) }
            launch { getUserUseCase.user.collectLatest(uiState::updateUser) }
        }
    }

    override fun showToast(text: Int) = uiState.showToast(text)
}

class MainRootUiState : BaseMainUIState() {
    var user: StableUser? by mutableStateOf(null)
        private set

    fun updateUser(user: User?) {
        this.user = user?.toStable()
    }
}
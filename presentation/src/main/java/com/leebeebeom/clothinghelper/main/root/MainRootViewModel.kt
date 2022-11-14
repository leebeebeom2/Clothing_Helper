package com.leebeebeom.clothinghelper.main.root

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.BaseIsAllExpandState
import com.leebeebeom.clothinghelper.main.base.EditSubCategoryNameViewModel
import com.leebeebeom.clothinghelper.map.StableUser
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.usecase.preferences.MainScreenRootAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.*
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainRootViewModel @Inject constructor(
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    private val updateSubCategoriesUseCase: UpdateSubCategoriesUseCase,
    private val mainScreenRootAllExpandUseCase: MainScreenRootAllExpandUseCase,
    private val getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase,
    private val getUserUseCase: GetUserUseCase,
    addSubCategoryUseCase: AddSubCategoryUseCase,
    editSubCategoryNameUseCase: EditSubCategoryNameUseCase
) : EditSubCategoryNameViewModel(editSubCategoryNameUseCase, addSubCategoryUseCase) {

    val uiStates = MainRootUiState()

    init {
        viewModelScope.launch {
            updateSubCategoriesUseCase().collect {
                if (it is FirebaseResult.Fail)
                    when (it.exception) {
                        is TimeoutCancellationException -> showToast(R.string.network_error_for_load)
                        else -> uiStates.showToast(R.string.sub_categories_load_failed)
                    }
            }
        }

        viewModelScope.launch {
            getSubCategoryLoadingStateUseCase().collect(uiStates::updateIsLoading)
        }

        viewModelScope.launch {
            getAllSubCategoriesUseCase().collect(uiStates::updateAllSubCategories)
        }

        viewModelScope.launch {
            mainScreenRootAllExpandUseCase.isAllExpand.collect(uiStates::updateIsAllExpand)
        }

        viewModelScope.launch {
            getUserUseCase().collect(uiStates::updateUser)
        }
    }

    fun toggleAllExpand() {
        viewModelScope.launch {
            mainScreenRootAllExpandUseCase.toggleAllExpand()
        }
    }

    override fun showToast(text: Int) {
        uiStates.showToast(text)
    }
}

class MainRootUiState : BaseIsAllExpandState() {
    var user: StableUser? by mutableStateOf(null)
        private set

    fun updateUser(user: User?) {
        this.user = user?.toStable()
    }
}
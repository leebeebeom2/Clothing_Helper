package com.leebeebeom.clothinghelper.ui.main.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.data.model.SubCategory
import com.leebeebeom.clothinghelper.domain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.UserInfoUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenRootViewModel @Inject constructor(
    private val userInfoUserCase: UserInfoUserCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase
) : ViewModel() {
    val viewModelState =
        MainNavHostViewModelState(userInfoUserCase.name.value, userInfoUserCase.email.value)

    init {
        viewModelScope.launch {
            userInfoUserCase.name.collect { viewModelState.nameUpdate(it) }
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
            getSubCategoriesUseCase.getETCSubCategories()
                .collect(viewModelState::etcSubCategoriesUpdate)
        }
    }
}

class MainNavHostViewModelState(name: String, val email: String) {
    var name by mutableStateOf(name)
        private set

    fun nameUpdate(name: String) {
        this.name = name
    }

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
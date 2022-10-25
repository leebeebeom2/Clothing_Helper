package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.usecase.preferences.GetPreferencesAndToggleAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.DeleteSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadAndGetSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val addSubCategoryUseCase: AddSubCategoryUseCase, // 두개 결합
    private val loadAndGetSubCategoriesUseCase: LoadAndGetSubCategoriesUseCase,
    private val deleteSubCategoryUseCase: DeleteSubCategoryUseCase,
    private val getPreferencesAndToggleAllExpandUseCase: GetPreferencesAndToggleAllExpandUseCase
) : ViewModel() {
    var viewModelState = SubCategoryViewModelState()
        private set

    init {
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.topSubCategories.collect(viewModelState::updateTopSubCategories)
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.bottomSubCategories.collect(viewModelState::updateBottomSubCategories)
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.outerSubCategories.collect(viewModelState::updateOuterSubCategories)
        }
        viewModelScope.launch {
            loadAndGetSubCategoriesUseCase.etcSubCategories.collect(viewModelState::updateEtcSubCategories)
        }
        viewModelScope.launch {
            getPreferencesAndToggleAllExpandUseCase.getPreferences(this).collect {
                viewModelState.updateAllExpand(it.allExpand)
                viewModelState.setAllExpandStates(it.allExpand)
            }
        }
    }

    fun addSubCategory(parent: SubCategoryParent, name: String) {
        addSubCategoryUseCase(parent, name, addSubCategoryListener)
        viewModelState.addExpandState()
    }

    private val addSubCategoryListener = object : FirebaseListener {
        override fun taskSuccess() {}
        override fun taskFailed(exception: Exception?) {} // TODO showToast 구현
    }

    fun toggleAllExpand() = viewModelScope.launch {
        getPreferencesAndToggleAllExpandUseCase.toggleAllExpand()
    }

    fun deleteSubCategory(subCategory: SubCategory) =
        deleteSubCategoryUseCase(subCategory, object : FirebaseListener {
            override fun taskSuccess() {
                // TODO
            }

            override fun taskFailed(exception: Exception?) {
                // TODO
            }

        })

}

class SubCategoryViewModelState {
    var allExpand by mutableStateOf(false)
        private set

    fun updateAllExpand(allExpand: Boolean) {
        this.allExpand = allExpand
    }

    private val expandStates = mutableListOf<MutableState<Boolean>>()

    fun setAllExpandStates(allExpand: Boolean) {
        for (isExpand in expandStates) isExpand.value = allExpand
    }

    fun getExpandState(index: Int): Boolean {
        if (expandStates.getOrNull(index) == null) expandStates.add(mutableStateOf(allExpand))
        return expandStates[index].value
    }

    fun expandToggle(index: Int) {
        expandStates[index].value = !expandStates[index].value
    }

    fun addExpandState() = expandStates.add(mutableStateOf(allExpand))

    private var topSubCategories by mutableStateOf(emptyList<SubCategory>())
    private var bottomSubCategories by mutableStateOf(emptyList<SubCategory>())
    private var outerSubCategories by mutableStateOf(emptyList<SubCategory>())
    private var etcSubCategories by mutableStateOf(emptyList<SubCategory>())

    fun updateTopSubCategories(topSubCategories: List<SubCategory>) {
        this.topSubCategories = topSubCategories
    }

    fun updateBottomSubCategories(bottomSubCategories: List<SubCategory>) {
        this.bottomSubCategories = bottomSubCategories
    }

    fun updateOuterSubCategories(outerSubCategories: List<SubCategory>) {
        this.outerSubCategories = outerSubCategories
    }

    fun updateEtcSubCategories(etcSubCategories: List<SubCategory>) {
        this.etcSubCategories = etcSubCategories
    }

    fun getSubCategories(parent: SubCategoryParent): List<SubCategory> {
        return when (parent) {
            SubCategoryParent.TOP -> topSubCategories
            SubCategoryParent.BOTTOM -> bottomSubCategories
            SubCategoryParent.OUTER -> outerSubCategories
            SubCategoryParent.ETC -> etcSubCategories
        }
    }
}
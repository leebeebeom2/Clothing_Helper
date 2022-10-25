package com.leebeebeom.clothinghelper.main.subcategory

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
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.usecase.preferences.GetPreferencesAndToggleAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddAndDeleteSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadAndGetSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val loadAndGetSubCategoriesUseCase: LoadAndGetSubCategoriesUseCase,
    private val addAndDeleteSubCategoryUseCase: AddAndDeleteSubCategoryUseCase,
    private val getPreferencesAndToggleAllExpandUseCase: GetPreferencesAndToggleAllExpandUseCase
) : ViewModel() {
    private val viewModelState = SubCategoryViewModelState()

    fun getViewModelState(parent: String): SubCategoryViewModelState {
        viewModelState.setSubCategoryParent(parent)
        return viewModelState
    }

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
            }
        }
    }

    fun addSubCategory(name: String) {
        addAndDeleteSubCategoryUseCase.addSubCategory(
            viewModelState.subCategoryParent,
            name,
            addSubCategoryListener
        )
    }

    private val addSubCategoryListener = object : FirebaseListener {
        override fun taskSuccess() {}
        override fun taskFailed(exception: Exception?) {
            viewModelState.showToast(R.string.add_category_failed)
            Log.d(TAG, "taskFailed: $exception")
        }
    }

    fun toggleAllExpand() = viewModelScope.launch {
        getPreferencesAndToggleAllExpandUseCase.toggleAllExpand()
    }

    fun deleteSubCategory(subCategory: SubCategory) =
        addAndDeleteSubCategoryUseCase.deleteSubCategory(subCategory, object : FirebaseListener {
            override fun taskSuccess() {}
            override fun taskFailed(exception: Exception?) {
                viewModelState.showToast(R.string.delete_category_failed)
                Log.d(TAG, "taskFailed: $exception")
            }
        })

}

class SubCategoryViewModelState : BaseViewModelState() {
    var allExpand by mutableStateOf(false)
        private set

    fun updateAllExpand(allExpand: Boolean) {
        this.allExpand = allExpand
    }

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

    fun getSubCategories(): List<SubCategory> {
        return when (subCategoryParent) {
            SubCategoryParent.TOP -> topSubCategories
            SubCategoryParent.BOTTOM -> bottomSubCategories
            SubCategoryParent.OUTER -> outerSubCategories
            SubCategoryParent.ETC -> etcSubCategories
        }
    }

    var subCategoryParent = SubCategoryParent.TOP // temp
        private set

    fun setSubCategoryParent(parent: String) {
        subCategoryParent = enumValueOf(parent)
    }

    var selectedSubCategories by mutableStateOf(setOf<SubCategory>())
        private set

    fun onSelect(subCategory: SubCategory) {
        selectedSubCategories = if (selectedSubCategories.contains(subCategory))
            selectedSubCategories.taskAndReturn { it.remove(subCategory) }
        else selectedSubCategories.taskAndReturn { it.add(subCategory) }
    }

    var selectMode by mutableStateOf(false)
        private set

    fun selectModeOn() {
        selectMode = true
    }

    fun selectModeOff() {
        selectMode = false
        selectedSubCategories = selectedSubCategories.taskAndReturn { it.clear() }
    }

    val isAllSelected get() = selectedSubCategories.size == getSubCategories().size

    fun toggleAllSelect() {
        selectedSubCategories =
            if (isAllSelected) selectedSubCategories.taskAndReturn { it.clear() }
            else selectedSubCategories.taskAndReturn { it.addAll(getSubCategories()) }
    }
}

fun <T> Set<T>.taskAndReturn(task: (MutableSet<T>) -> Unit): Set<T> {
    val temp = toMutableSet()
    task(temp)
    return temp
}
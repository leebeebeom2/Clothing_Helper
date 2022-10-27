package com.leebeebeom.clothinghelper.main.subcategory

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.main.base.BaseSubCategoriesViewModel
import com.leebeebeom.clothinghelper.main.base.BaseSubCategoriesViewModelState
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.usecase.preferences.GetPreferencesAndToggleAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.LoadAndGetSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    loadAndGetSubCategoriesUseCase: LoadAndGetSubCategoriesUseCase,
    private val addSubCategoryUseCase: AddSubCategoryUseCase,
    private val getPreferencesAndToggleAllExpandUseCase: GetPreferencesAndToggleAllExpandUseCase
) : BaseSubCategoriesViewModel(loadAndGetSubCategoriesUseCase) {

    override val viewModelState = SubCategoryViewModelState()

    init {
        collectSubCategories()

        viewModelScope.launch {
            getPreferencesAndToggleAllExpandUseCase.getPreferences(scope = this).collect {
                viewModelState.updateAllExpand(it.allExpand)
            }
        }
    }

    fun addSubCategory(name: String, subCategoryParent: SubCategoryParent) =
        addSubCategoryUseCase(
            subCategoryParent = subCategoryParent,
            name = name,
            addSubCategoryListener = addSubCategoryListener
        )

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
}

class SubCategoryViewModelState : BaseSubCategoriesViewModelState() {
    var allExpand by mutableStateOf(false)
        private set

    fun updateAllExpand(allExpand: Boolean) {
        this.allExpand = allExpand
    }
}
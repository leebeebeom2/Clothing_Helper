package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.main.base.BaseMainUIState
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.util.taskAndReturnSet
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategorySortUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryNameUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetAllSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoryLoadingStateUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val getSubCategoryLoadingStateUseCase: GetSubCategoryLoadingStateUseCase,
    private val subCategorySortUseCase: SubCategorySortUseCase,
    private val getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase,
    private val getUserUseCase: GetUserUseCase,
    editSubCategoryNameUseCase: EditSubCategoryNameUseCase,
    addSubCategoryUseCase: AddSubCategoryUseCase,
) : EditSubCategoryNameViewModel(editSubCategoryNameUseCase, addSubCategoryUseCase) {

    private val uiState = SubCategoryUIState()

    fun getUiState(parent: SubCategoryParent): SubCategoryUIState {
        uiState.setParent(parent)
        return uiState
    }

    init {
        viewModelScope.launch {
            launch { getSubCategoryLoadingStateUseCase.isLoading.collectLatest(uiState::updateIsLoading) }
            launch { getAllSubCategoriesUseCase.allSubCategories.collectLatest(uiState::loadAllSubCategories) }
            launch { subCategorySortUseCase.sortPreferences.collectLatest(uiState::updateSort) }
            launch { getUserUseCase.user.collectLatest(uiState::updateUser) }
        }
    }

    override fun showToast(text: Int) = uiState.showToast(text)
    override val uid get() = uiState.user?.uid
    fun changeSort(sort: Sort) = viewModelScope.launch { subCategorySortUseCase.changeSort(sort) }
    fun changeOrder(order: Order) =
        viewModelScope.launch { subCategorySortUseCase.changeOrder(order) }

    override fun editSubCategoryName(newSubCategory: StableSubCategory) {
        viewModelScope.launch {
            uiState.selectModeOff()
            super.editSubCategoryName(newSubCategory)
        }
    }
}

class SubCategoryUIState : BaseMainUIState() {
    var parent = SubCategoryParent.TOP
        private set
    var sort by mutableStateOf(SortPreferences())
        private set

    fun updateSort(sort: SortPreferences) {
        this.sort = sort
    }

    var isSelectMode by mutableStateOf(false)
        private set

    var selectedSubCategoryKeys by mutableStateOf(linkedSetOf<String>().toImmutableSet())
        private set
    val selectedSubCategoriesSize by derivedStateOf { selectedSubCategoryKeys.size }

    val firstSelectedSubCategory by derivedStateOf {
        selectedSubCategoryKeys.firstOrNull()
            ?.let { key -> subCategories.firstOrNull { it.key == key } }
    }

    var subCategories by mutableStateOf(emptyList<StableSubCategory>().toImmutableList())
        private set
    var subCategoryNames by mutableStateOf(emptyList<String>().toImmutableList())
        private set

    val isAllSelected by derivedStateOf { selectedSubCategoryKeys.size == subCategories.size }
    val showEditIcon by derivedStateOf { selectedSubCategoriesSize == 1 }
    val showDeleteIcon by derivedStateOf { selectedSubCategoriesSize > 0 }

    fun setParent(parent: SubCategoryParent) {
        this.parent = parent
        subCategories = getSubCategories(parent)
        subCategoryNames = getSubCategoryNames(parent)
    }

    fun toggleAllSelect() {
        selectedSubCategoryKeys =
            if (selectedSubCategoriesSize == subCategories.size) emptySet<String>().toImmutableSet()
            else subCategories.map { it.key }.toImmutableSet()

    }

    fun onSelect(key: String) {
        selectedSubCategoryKeys =
            if (selectedSubCategoryKeys.contains(key)) selectedSubCategoryKeys.taskAndReturnSet {
                it.remove(key)
            }
            else selectedSubCategoryKeys.taskAndReturnSet { it.add(key) }
    }

    fun selectModeOn(key: String) {
        onSelect(key)
        isSelectMode = true
    }

    suspend fun selectModeOff() {
        isSelectMode = false
        delay(200)
        selectedSubCategoryKeys = selectedSubCategoryKeys.taskAndReturnSet { it.clear() }
    }
}
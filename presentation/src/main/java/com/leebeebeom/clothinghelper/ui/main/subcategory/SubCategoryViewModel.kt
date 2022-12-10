package com.leebeebeom.clothinghelper.ui.main.subcategory

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.state.ToastUIState
import com.leebeebeom.clothinghelper.state.ToastUIStateImpl
import com.leebeebeom.clothinghelper.ui.main.interfaces.*
import com.leebeebeom.clothinghelper.ui.main.interfaces.addandedit.subcategory.AddSubCategory
import com.leebeebeom.clothinghelper.ui.main.interfaces.addandedit.subcategory.EditSubCategory
import com.leebeebeom.clothinghelper.ui.main.interfaces.container.FolderUIState
import com.leebeebeom.clothinghelper.ui.main.interfaces.container.FolderUIStateImpl
import com.leebeebeom.clothinghelper.ui.main.interfaces.container.SubCategoryUIState
import com.leebeebeom.clothinghelper.ui.main.interfaces.container.SubCategoryUIStateImpl
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.GetDataLoadingStateUseCase
import com.leebeebeom.clothinghelperdomain.usecase.folder.GetAllFoldersUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preference.sort.SubCategorySortUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetAllSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val getDataLoadingStateUseCase: GetDataLoadingStateUseCase,
    private val subCategorySortUseCase: SubCategorySortUseCase,
    private val getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    override val addSubCategoryUseCase: AddSubCategoryUseCase,
    override val editSubCategoryUseCase: EditSubCategoryUseCase,
) : AddSubCategory, EditSubCategory, ViewModel() {

    private val uiState = SubCategoryScreenUIState()
    override val toastState get() = uiState
    override val userState get() = uiState

    fun getUiState(parent: SubCategoryParent) = uiState.setSubCategories(parent)

    init {
        viewModelScope.launch {
            launch { getDataLoadingStateUseCase.isLoading.collectLatest(uiState::updateIsLoading) }
            launch { getAllSubCategoriesUseCase.allSubCategories.collectLatest(uiState::loadAllSubCategories) }
            launch { subCategorySortUseCase.sortPreferences.collectLatest(uiState::updateSort) }
            launch { getUserUseCase.user.collectLatest(uiState::updateUser) }
            launch { getAllFoldersUseCase.allFolders.collectLatest(uiState::loadAllFolders) }
        }
    }

    fun changeSort(sort: Sort) = viewModelScope.launch { subCategorySortUseCase.changeSort(sort) }

    fun changeOrder(order: Order) =
        viewModelScope.launch { subCategorySortUseCase.changeOrder(order) }

    fun addSubCategory(name: String, parent: SubCategoryParent) =
        viewModelScope.launch { super.baseAddSubCategory(name = name, parent = parent) }

    fun editSubCategoryName(oldSubCategory: StableSubCategory, name: String) =
        viewModelScope.launch {
            uiState.selectModeOff(this)
            super.baseEditSubCategory(oldSubCategory = oldSubCategory, name = name)
        }
}

class SubCategoryScreenUIState(private val selectModeImpl: SelectModeImpl<StableSubCategory> = SelectModeImpl()) :
    ToastUIState by ToastUIStateImpl(), LoadingUIState by LoadingUIStateImpl(),
    UserUIState by UserUIStateImpl(), SubCategoryUIState by SubCategoryUIStateImpl(),
    SortUIState by SortUIStateImpl(), SelectMode<StableSubCategory> by selectModeImpl,
    FolderUIState by FolderUIStateImpl() {

    var subCategories by mutableStateOf(emptyList<StableSubCategory>().toImmutableList())
        private set
    val itemsSize by derivedStateOf { subCategories.size }

    override val firstSelectedItem get() = selectModeImpl.getFirstSelectedItem(subCategories)

    override fun toggleAllSelect() = selectModeImpl.toggleAllSelect(subCategories)

    fun setSubCategories(parent: SubCategoryParent): SubCategoryScreenUIState {
        subCategories = getSubCategories(parent.name)
        return this
    }
}
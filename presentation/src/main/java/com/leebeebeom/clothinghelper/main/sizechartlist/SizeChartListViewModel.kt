package com.leebeebeom.clothinghelper.main.sizechartlist

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.base.ToastUIState
import com.leebeebeom.clothinghelper.base.ToastUIStateImpl
import com.leebeebeom.clothinghelper.main.base.interfaces.*
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.folder.AddFolder
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.folder.EditFolder
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.folder.AddFolderUseCase
import com.leebeebeom.clothinghelperdomain.usecase.folder.EditFolderNameUseCase
import com.leebeebeom.clothinghelperdomain.usecase.folder.GetAllFoldersUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.sort.FolderSortUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetAllSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SizeChartListViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getUserUseCase: GetUserUseCase,
    override val addFoldersUseCase: AddFolderUseCase,
    override val editFolderNameUseCase: EditFolderNameUseCase,
    private val folderSortUseCase: FolderSortUseCase,
    private val getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase
) : ViewModel(), AddFolder, EditFolder {

    private val uiState = SizeChartListUIState()

    fun getUIState(parentKey: String) = uiState.setParentKey(parentKey)

    init {
        viewModelScope.launch {
            launch { getAllFoldersUseCase.allFolders.collectLatest(uiState::loadAllFolders) }
            launch { getUserUseCase.user.collectLatest(uiState::updateUser) }
            launch { folderSortUseCase.sortPreferences.collectLatest(uiState::updateSort) }
            launch { getAllSubCategoriesUseCase.allSubCategories.collectLatest(uiState::loadAllSubCategories) }
        }
    }

    fun addFolder(
        parentKey: String,
        subCategoryKey: String,
        name: String,
        parent: SubCategoryParent
    ) = viewModelScope.launch { baseAddFolder(parentKey, subCategoryKey, name, parent) }

    fun editFolder(oldFolder: StableFolder, name: String) = viewModelScope.launch {
        uiState.selectModeOff()
        this@SizeChartListViewModel.baseEditFolder(oldFolder, name)
    }

    fun changeSort(sort: Sort) = viewModelScope.launch { folderSortUseCase.changeSort(sort) }
    fun changeOrder(order: Order) = viewModelScope.launch { folderSortUseCase.changeOrder(order) }

    override fun showToast(text: Int) = uiState.showToast(text)

    override val uid get() = uiState.user?.uid
}

class SizeChartListUIState(private val selectModeImpl: SelectModeImpl<StableFolder> = SelectModeImpl()) :
    UserUIState by UserUIStateImpl(),
    ToastUIState by ToastUIStateImpl(),
    LoadingUIState by LoadingUIStateImpl(),
    FolderUIState by FolderUIStateImpl(),
    SortUIState by SortUIStateImpl(),
    SubCategoryUIState by SubCategoryUIStateImpl(),
    SelectMode<StableFolder> by selectModeImpl {

    override var items by mutableStateOf(emptyList<StableFolder>().toImmutableList())
        private set
    var title = ""
        private set
    var parent = SubCategoryParent.TOP
        private set

    override val isAllSelected by derivedStateOf { selectedKeys.size == items.size }

    override val firstSelectedItem get() = items.first { it.key == selectedKeys.first() }

    override fun toggleAllSelect() = selectModeImpl.toggleAllSelect(items)

    fun setParentKey(parentKey: String): SizeChartListUIState {
        items = getFolders(parentKey)
        title = getTitle(parentKey)
        return this
    }

    private fun getTitle(parentKey: String): String {
        val sb = StringBuilder()
        var lastParentKey = parentKey
        var end = false

        while (!end) {
            val folderParent = allFolders.firstOrNull { it.key == lastParentKey }
            if (folderParent != null) {
                lastParentKey = folderParent.parentKey
                sb.insert(0, " - ${folderParent.name}")
            } else {
                val subCategoryParent = allSubCategories.first { it.key == lastParentKey }
                sb.insert(0, " - ${subCategoryParent.name}")
                parent = subCategoryParent.parent
                sb.insert(0, "${parent.name}s")
                end = true
            }
        }
        return sb.toString()
    }
}
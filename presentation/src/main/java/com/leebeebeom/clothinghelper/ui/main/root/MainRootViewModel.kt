package com.leebeebeom.clothinghelper.ui.main.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.state.ToastUIState
import com.leebeebeom.clothinghelper.state.ToastUIStateImpl
import com.leebeebeom.clothinghelper.ui.main.interfaces.LoadingUIState
import com.leebeebeom.clothinghelper.ui.main.interfaces.LoadingUIStateImpl
import com.leebeebeom.clothinghelper.ui.main.interfaces.UserUIState
import com.leebeebeom.clothinghelper.ui.main.interfaces.UserUIStateImpl
import com.leebeebeom.clothinghelper.ui.main.interfaces.addandedit.folder.AddFolder
import com.leebeebeom.clothinghelper.ui.main.interfaces.addandedit.folder.EditFolder
import com.leebeebeom.clothinghelper.ui.main.interfaces.addandedit.subcategory.AddSubCategory
import com.leebeebeom.clothinghelper.ui.main.interfaces.addandedit.subcategory.EditSubCategory
import com.leebeebeom.clothinghelper.ui.main.interfaces.container.FolderUIState
import com.leebeebeom.clothinghelper.ui.main.interfaces.container.FolderUIStateImpl
import com.leebeebeom.clothinghelper.ui.main.interfaces.container.SubCategoryUIState
import com.leebeebeom.clothinghelper.ui.main.interfaces.container.SubCategoryUIStateImpl
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.GetDataLoadingStateUseCase
import com.leebeebeom.clothinghelperdomain.usecase.LoadDataUseCase
import com.leebeebeom.clothinghelperdomain.usecase.folder.AddFolderUseCase
import com.leebeebeom.clothinghelperdomain.usecase.folder.EditFolderNameUseCase
import com.leebeebeom.clothinghelperdomain.usecase.folder.GetAllFoldersUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetAllSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainRootViewModel @Inject constructor(
    private val getDataLoadingStateUseCase: GetDataLoadingStateUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    override val addSubCategoryUseCase: AddSubCategoryUseCase,
    override val editSubCategoryUseCase: EditSubCategoryUseCase,
    override val addFoldersUseCase: AddFolderUseCase,
    override val editFolderNameUseCase: EditFolderNameUseCase
) : AddSubCategory, EditSubCategory, AddFolder, EditFolder, ViewModel() {

    val uiState = MainRootUIState()
    override val toastState = uiState
    override val userState = uiState

    init {
        viewModelScope.launch {
            launch {
                loadDataUseCase.load { uiState.showToast(R.string.data_load_failed) }
            }

            launch { getDataLoadingStateUseCase.isLoading.collectLatest(uiState::updateIsLoading) }
            launch { getUserUseCase.user.collectLatest(uiState::updateUser) }
            launch { getAllSubCategoriesUseCase.allSubCategories.collectLatest(uiState::loadAllSubCategories) }
            launch { getAllFoldersUseCase.allFolders.collectLatest(uiState::loadAllFolders) }
        }
    }

    fun addSubCategory(name: String, parent: SubCategoryParent) {
        viewModelScope.launch {
            super.baseAddSubCategory(name = name, parent = parent)
        }
    }

    fun editSubCategoryName(oldSubCategory: StableSubCategory, name: String) {
        viewModelScope.launch {
            super.baseEditSubCategory(oldSubCategory = oldSubCategory, name = name)
        }
    }

    fun addFolder(
        parentKey: String, subCategoryKey: String, name: String, parent: SubCategoryParent
    ) {
        viewModelScope.launch {
            super.baseAddFolder(
                parentKey = parentKey,
                subCategoryKey = subCategoryKey,
                name = name,
                subCategoryParent = parent,
            )
        }
    }

    fun editFolder(oldFolder: StableFolder, name: String) {
        viewModelScope.launch {
            super.baseEditFolder(oldFolder = oldFolder, name = name)
        }
    }
}

class MainRootUIState : ToastUIState by ToastUIStateImpl(), UserUIState by UserUIStateImpl(),
    LoadingUIState by LoadingUIStateImpl(), SubCategoryUIState by SubCategoryUIStateImpl(),
    FolderUIState by FolderUIStateImpl()
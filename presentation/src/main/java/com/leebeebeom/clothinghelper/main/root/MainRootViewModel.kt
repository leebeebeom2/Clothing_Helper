package com.leebeebeom.clothinghelper.main.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.ToastUIState
import com.leebeebeom.clothinghelper.base.ToastUIStateImpl
import com.leebeebeom.clothinghelper.main.base.interfaces.*
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.folder.AddFolder
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.folder.EditFolder
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.subcategory.AddSubCategory
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.subcategory.EditSubCategory
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
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
import kotlinx.coroutines.TimeoutCancellationException
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

    init {
        viewModelScope.launch {
            launch {
                loadDataUseCase.load {
                    when (it.exception) {
                        is TimeoutCancellationException -> showToast(R.string.network_error_for_load)
                        else -> uiState.showToast(R.string.data_load_failed)
                    }
                }
            }

            launch { getDataLoadingStateUseCase.isLoading.collectLatest(uiState::updateIsLoading) }
            launch { getAllSubCategoriesUseCase.allSubCategories.collectLatest(uiState::loadAllSubCategories) }
            launch { getUserUseCase.user.collectLatest(uiState::updateUser) }
            launch { getAllFoldersUseCase.allFolders.collectLatest(uiState::loadAllFolders) }
        }
    }

    fun addSubCategory(subCategory: StableSubCategory) {
        viewModelScope.launch {
            super.baseAddSubCategory(subCategory)
        }
    }

    fun editSubCategoryName(subCategory: StableSubCategory) {
        viewModelScope.launch {
            super.baseEditSubCategory(subCategory)
        }
    }

    fun addFolder(folder: StableFolder) {
        viewModelScope.launch {
            super.baseAddFolder(folder)
        }
    }

    fun editFolder(newFolder:StableFolder){
        viewModelScope.launch {
            super.baseEditFolder(newFolder)
        }
    }

    override fun showToast(text: Int) = uiState.showToast(text)
    override val uid get() = uiState.user?.uid
}

class MainRootUIState :
    ToastUIState by ToastUIStateImpl(),
    UserUIState by UserUIStateImpl(),
    LoadingUIState by LoadingUIStateImpl(),
    SubCategoryUIState by SubCategoryUIStateImpl(),
    FolderUIState by FolderUIStateImpl()
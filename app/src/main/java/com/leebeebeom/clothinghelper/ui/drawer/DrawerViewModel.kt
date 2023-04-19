package com.leebeebeom.clothinghelper.ui.drawer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.MenuType
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.usecase.folder.AddFolderUseCase
import com.leebeebeom.clothinghelper.domain.usecase.folder.DeleteFolderUseCase
import com.leebeebeom.clothinghelper.domain.usecase.folder.EditFolderNameUseCase
import com.leebeebeom.clothinghelper.domain.usecase.folder.GetFolderNamesMapFlowUseCase
import com.leebeebeom.clothinghelper.domain.usecase.folder.GetFoldersMapFlowUseCase
import com.leebeebeom.clothinghelper.domain.usecase.folder.GetFoldersSizeMapFlowUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import com.leebeebeom.clothinghelper.ui.state.FolderNamesState
import com.leebeebeom.clothinghelper.ui.state.FoldersSizeState
import com.leebeebeom.clothinghelper.ui.state.FoldersState
import com.leebeebeom.clothinghelper.ui.util.toastHandler
import com.leebeebeom.clothinghelper.ui.viewmodel.ToastViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DrawerToastTextsKey = "drawer toast texts"

@HiltViewModel
class DrawerViewModel @Inject constructor(
    getFoldersMapFlowUseCase: GetFoldersMapFlowUseCase,
    getFolderNamesMapFlowUseCase: GetFolderNamesMapFlowUseCase,
    getFoldersSizeMapFlowUseCase: GetFoldersSizeMapFlowUseCase,
    getUserUseCase: GetUserUseCase,
    private val addFoldersUseCase: AddFolderUseCase,
    private val editFolderNameUseCase: EditFolderNameUseCase,
    private val deleteFolderNameUseCase: DeleteFolderUseCase,
    savedStateHandle: SavedStateHandle
) : ToastViewModel(
    savedToastTextsKey = DrawerToastTextsKey,
    savedStateHandle = savedStateHandle
) {

    val uiState = combine(
        flow = getFoldersMapFlowUseCase.foldersMapFlow,
        flow2 = getFolderNamesMapFlowUseCase.folderNamesMapFlow,
        flow3 = getFoldersSizeMapFlowUseCase.foldersSizeMapFolder,
        flow4 = toastTextsFlow,
        flow5 = getUserUseCase.userFlow,
    ) { foldersMap, folderNames, foldersSize, toastTexts, user ->
        // TODO FolderResultMap.Fail 시 토스트

        DrawerUiState(
            isLoading = false,
            foldersMap = foldersMap.data,
            folderNamesMap = folderNames,
            foldersSizeMap = foldersSize,
            toastTexts = toastTexts,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DrawerUiState()
    )

    fun addFolder(parentKey: String, name: String, menuType: MenuType) {
        viewModelScope.launch(
            toastHandler(callSite = "DrawerViewModel.addFolder",
                showToast = { addToastTextAtLast(R.string.error_add_folder_failed) })
        ) {
            addFoldersUseCase.add(
                parentKey = parentKey, name = name, menuType = menuType
            )
        }
    }

    fun editFolderName(oldFolder: Folder, name: String) {
        viewModelScope.launch(
            toastHandler("DrawerViewModel.editFolderName",
                showToast = { addToastTextAtLast(R.string.error_edit_folder_failed) })
        ) {
            editFolderNameUseCase.editName(oldFolder, name)
        }
    }

    fun deletedFolder(folder: Folder) {
        viewModelScope.launch(
            toastHandler("DrawerViewModel.deleteFolder",
                showToast = { addToastTextAtLast(R.string.error_edit_folder_failed) })
        ) {
            deleteFolderNameUseCase.delete(folder)
        }
    }
}

data class DrawerUiState(
    val isLoading: Boolean = true,
    val toastTexts: ImmutableList<Int> = persistentListOf(),
    val user: User? = null,
    override val foldersMap: ImmutableMap<String, ImmutableList<Folder>> = persistentMapOf(),
    override val folderNamesMap: ImmutableMap<String, ImmutableSet<String>> = persistentMapOf(),
    override val foldersSizeMap: ImmutableMap<String, Int> = persistentMapOf(),
) : FoldersState, FolderNamesState, FoldersSizeState
package com.leebeebeom.clothinghelper.ui.drawer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.repository.DataResultMap
import com.leebeebeom.clothinghelper.domain.usecase.folder.*
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import com.leebeebeom.clothinghelper.ui.util.toastHandler
import com.leebeebeom.clothinghelper.ui.viewmodel.ToastViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.*
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
    getFolderSizeMapFlowUseCase: GetFolderSizeMapFlowUseCase,
    getUserUseCase: GetUserUseCase,
    private val addFoldersUseCase: AddFolderUseCase,
    private val editFolderNameUseCase: EditFolderNameUseCase,
    savedStateHandle: SavedStateHandle
) : ToastViewModel(
    savedToastTextsKey = DrawerToastTextsKey,
    savedStateHandle = savedStateHandle
) {

    val uiState = combine(
        flow = getFoldersMapFlowUseCase.foldersMapFlow,
        flow2 = getFolderNamesMapFlowUseCase.folderNamesMapFlow,
        flow3 = getFolderSizeMapFlowUseCase.folderSizeMapFolder,
        flow4 = toastTextsFlow,
        flow5 = getUserUseCase.userFlow,
    ) { foldersMap, folderNames, folderSize, toastTexts, user ->
        when { // TODO 현재 무한 순환이며 데이터 발행마다 에러 메세지 나감 설계 미스
            foldersMap is DataResultMap.Fail -> {
                // TODO 로그
                addToastTextAtLast(R.string.error_fail_get_data_by_unknow_error)
            }
        }

        DrawerUiState(
            isLoading = false,
            foldersMap = foldersMap.data,
            folderNamesMap = folderNames,
            foldersSizeMap = folderSize,
            toastTexts = toastTexts,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DrawerUiState()
    )

    fun addFolder(parentKey: String, name: String) {
        viewModelScope.launch(
            toastHandler(callSite = "DrawerViewModel.addFolder",
                showToast = { addToastTextAtLast(R.string.error_add_folder_failed) })
        ) {
            addFoldersUseCase.add(
                parentKey = parentKey,
                name = name
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
}

data class DrawerUiState(
    val isLoading: Boolean = true,
    val foldersMap: ImmutableMap<String, ImmutableList<Folder>> = persistentMapOf(),
    val folderNamesMap: ImmutableMap<String, ImmutableSet<String>> = persistentMapOf(),
    val foldersSizeMap: ImmutableMap<String, Int> = persistentMapOf(),
    val toastTexts: ImmutableList<Int> = persistentListOf(),
    val user: User? = null
) {
    fun getFolders(key: String) =
        foldersMap.getOrDefault(key = key, defaultValue = persistentListOf())

    fun getFolderNames(key: String) =
        folderNamesMap.getOrDefault(key = key, defaultValue = persistentSetOf())

    fun getFoldersSize(key: String) = foldersSizeMap.getOrDefault(key = key, defaultValue = 0)
}
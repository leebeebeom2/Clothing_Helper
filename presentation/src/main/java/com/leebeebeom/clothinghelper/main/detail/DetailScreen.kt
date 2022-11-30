package com.leebeebeom.clothinghelper.main.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.main.base.composables.SelectModeBackHandler
import com.leebeebeom.clothinghelper.main.base.composables.selectmodebottomappbar.SelectModeBottomAppBar
import com.leebeebeom.clothinghelper.main.base.dialogs.EditFolderDialog
import com.leebeebeom.clothinghelper.main.detail.contents.DetailHeader
import com.leebeebeom.clothinghelper.main.detail.contents.folder.FolderGrid
import com.leebeebeom.clothinghelper.map.StableFolder

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailScreen(
    parentKey: String,
    subCategoryKey: String,
    viewModel: DetailViewModel = hiltViewModel(),
    uiState: DetailUIState = viewModel.getUIState(parentKey),
    onFolderClick: (StableFolder) -> Unit,
    onFabClick: (path:String) -> Unit
) {
    var isFabSpread by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { isFabSpread = false })
    ) {
        val backDropState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
        BackdropScaffold(
            scaffoldState = backDropState,
            appBar = { DetailHeader(uiState.title) },
            backLayerBackgroundColor = MaterialTheme.colors.background,
            backLayerContent = {
                FolderGrid(
                    parentKey = parentKey,
                    folders = uiState::getFolders,
                    selectedFolderKeys = { uiState.selectedKeys },
                    isSelectMode = { uiState.isSelectMode },
                    sort = { uiState.sort },
                    onSortClick = viewModel::changeSort,
                    onOrderClick = viewModel::changeOrder,
                    onSelect = uiState::onSelect,
                    onLongClick = uiState::selectModeOn,
                    onClick = onFolderClick
                )
            },
            frontLayerContent = { FrontLayerContent() },
            frontLayerElevation = 1.dp,
            frontLayerScrimColor = Color.Unspecified,
            headerHeight = 120.dp
        )

        val addFolderPositiveClick = remember<(String) -> Unit> {
            {
                viewModel.addFolder(
                    StableFolder(
                        parentKey = parentKey,
                        subCategoryKey = subCategoryKey,
                        name = it,
                        parent = uiState.parent
                    )
                )
            }
        }
        DetailFab(
            onAddFolderPositiveButtonClick = addFolderPositiveClick,
            folders = { uiState.items },
            isRevealed = { backDropState.isRevealed },
            isSelectMode = { uiState.isSelectMode },
            onClick = { onFabClick(uiState.title) }
        )

        var showEditDialog by rememberSaveable { mutableStateOf(false) }

        SelectModeBottomAppBar(selectedSize = { uiState.selectedSize },
            isAllSelected = { uiState.isAllSelected },
            onAllSelectCheckBoxClick = uiState::toggleAllSelect,
            onEditIconClick = { showEditDialog = true },
            isSelectMode = { uiState.isSelectMode },
            showEditIcon = { uiState.showEditIcon },
            showDeleteIcon = { uiState.showDeleteIcon })

        EditFolderDialog(
            folders = { uiState.items },
            show = { showEditDialog },
            onDismiss = { showEditDialog = false },
            onPositiveButtonClick = viewModel::editFolder,
            folder = { uiState.firstSelectedItem }
        )
    }


    SelectModeBackHandler(
        isSelectMode = { uiState.isSelectMode }, selectModeOff = uiState::selectModeOff
    )
}
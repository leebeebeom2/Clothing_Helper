package com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.CurrentBackStack
import com.leebeebeom.clothinghelper.ui.util.DeleteFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import com.leebeebeom.clothinghelper.ui.util.FolderNames
import com.leebeebeom.clothinghelper.ui.util.Folders
import com.leebeebeom.clothinghelper.ui.util.FoldersSize
import com.leebeebeom.clothinghelper.ui.util.ItemsSize
import com.leebeebeom.clothinghelper.ui.util.OnClothesCategoryClick
import com.leebeebeom.clothinghelper.ui.util.OnFolderClick
import com.leebeebeom.clothinghelper.ui.util.OnMainMenuClick
import com.leebeebeom.clothinghelper.ui.util.OnSubMenuClick
import kotlinx.collections.immutable.persistentListOf

@Composable // skippable
fun DrawerMainMenus(
    onMainMenuClick: OnMainMenuClick,
    onSubMenuClick: OnSubMenuClick,
    onClothesCategoryClick: OnClothesCategoryClick,
    onFolderClick: OnFolderClick,
    folders: Folders,
    folderNames: FolderNames,
    foldersSize: FoldersSize,
    itemsSize: ItemsSize,
    addFolder: AddFolder,
    editFolder: EditFolder,
    deleteFolder: DeleteFolder,
    currentBackStack: CurrentBackStack
) {
    var height by rememberSaveable { mutableStateOf(0) }
    val density = LocalDensity.current
    val heightDp by remember(density) { derivedStateOf { with(density) { height.toDp() } } }

    Column {
        rememberMainMenus().forEach { mainMenu ->
            key(mainMenu.type) {
                if (mainMenu.type != MainMenuType.Archive) {
                    val state = rememberDrawerItemState()

                    DrawerMainMenu(
                        mainMenu = mainMenu,
                        onMainMenuClick = onMainMenuClick,
                        state = state,
                        height = { heightDp },
                        currentBackStack = currentBackStack,
                        toggleExpand = state::toggleExpand,
                        expand = state::expand,
                        onSubMenuClick = onSubMenuClick,
                        onClothesCategoryClick = onClothesCategoryClick,
                        onFolderClick = onFolderClick,
                        folders = folders,
                        folderNames = folderNames,
                        foldersSize = foldersSize,
                        itemsSize = itemsSize,
                        addFolder = addFolder,
                        editFolder = editFolder,
                        deleteFolder = deleteFolder
                    )
                } else {
                    val state = rememberDrawerItemDropdownMenuState()

                    DrawerArchive(
                        state = state,
                        onClick = onMainMenuClick,
                        onFolderClick = onFolderClick,
                        folderNames = folderNames,
                        foldersSize = foldersSize,
                        itemsSize = itemsSize,
                        addFolder = addFolder,
                        folders = folders,
                        onSizeChanged = { height = it },
                        currentBackStack = currentBackStack,
                        onLongClick = state::onLongClick,
                        onChildSizeChange = state::onSizeChanged,
                        toggleExpand = state::toggleExpand,
                        onDismissDropdownMenu = state::onDismissDropdownMenu,
                        expand = state::expand,
                        deleteFolder = deleteFolder,
                        editFolder = editFolder
                    )
                }
            }
        }
    }
}

@Composable
fun drawerMainMenuTextStyle() = MaterialTheme.typography.h6

data class MainMenu(
    @StringRes val name: Int,
    val type: MainMenuType
)

enum class MainMenuType {
    Brand, Clothes, Outfit, Archive
}

@Composable
fun rememberMainMenus() = remember {
    persistentListOf(
        MainMenu(R.string.brand_cap, MainMenuType.Brand),
        MainMenu(R.string.outfit_cap, MainMenuType.Outfit),
        MainMenu(R.string.clothes_cap, MainMenuType.Clothes),
        MainMenu(R.string.archive_cap, MainMenuType.Archive)
    )
}
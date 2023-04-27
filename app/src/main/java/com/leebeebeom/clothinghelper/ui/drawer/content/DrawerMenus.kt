package com.leebeebeom.clothinghelper.ui.drawer.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.drawerMainMenus
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.rememberMainMenus
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.CurrentBackStack
import com.leebeebeom.clothinghelper.ui.util.DeleteFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import com.leebeebeom.clothinghelper.ui.util.FolderNames
import com.leebeebeom.clothinghelper.ui.util.Folders
import com.leebeebeom.clothinghelper.ui.util.FoldersSize
import com.leebeebeom.clothinghelper.ui.util.ItemsSize
import com.leebeebeom.clothinghelper.ui.util.OnClothesCategoryClick
import com.leebeebeom.clothinghelper.ui.util.OnEssentialMenuClick
import com.leebeebeom.clothinghelper.ui.util.OnFolderClick
import com.leebeebeom.clothinghelper.ui.util.OnMainMenuClick
import com.leebeebeom.clothinghelper.ui.util.OnSubMenuClick

const val DrawerTag = "drawer"

@Composable // skippable
fun DrawerMenus(
    closeDrawer: () -> Unit,
    onEssentialMenuClick: OnEssentialMenuClick,
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
    val essentialMenus = rememberEssentialMenus()
    val mainMenus = rememberMainMenus()

    var mainMenuHeight by rememberSaveable { mutableStateOf(0) }
    val density = LocalDensity.current
    val mainMenuHeightDp by remember(density) { derivedStateOf { with(density) { mainMenuHeight.toDp() } } }

    LazyColumn(
        modifier = Modifier
            .testTag(DrawerTag)
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 40.dp)
    ) {
        drawerEssentialMenus(
            onEssentialMenuClick = {
                closeDrawer()
                onEssentialMenuClick(it)
            },
            currentBackStack = currentBackStack,
            essentialMenus = essentialMenus
        )

        drawerMainMenus(
            mainMenus = mainMenus,
            onMainMenuClick = {
                closeDrawer()
                onMainMenuClick(it)
            },
            onSubMenuClick = {
                closeDrawer()
                onSubMenuClick(it)
            },
            onClothesCategoryClick = {
                closeDrawer()
                onClothesCategoryClick(it)
            },
            onFolderClick = {
                closeDrawer()
                onFolderClick(it)
            },
            folders = folders,
            folderNames = folderNames,
            foldersSize = foldersSize,
            itemsSize = itemsSize,
            addFolder = addFolder,
            editFolder = editFolder,
            deleteFolder = deleteFolder,
            currentBackStack = currentBackStack,
            height = { mainMenuHeightDp },
            onSizeChanged = { mainMenuHeight = it }
        )
    }
}
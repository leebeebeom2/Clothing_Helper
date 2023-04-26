package com.leebeebeom.clothinghelper.ui.drawer.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.DrawerMainMenus
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
    Column(
        modifier = Modifier
            .testTag(DrawerTag)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(bottom = 40.dp)
    ) {
        DrawerEssentialMenus(
            onEssentialMenuClick = {
                closeDrawer()
                onEssentialMenuClick(it)
            },
            currentBackStack = currentBackStack
        )

        DrawerMainMenus(
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
            currentBackStack = currentBackStack
        )
    }
}
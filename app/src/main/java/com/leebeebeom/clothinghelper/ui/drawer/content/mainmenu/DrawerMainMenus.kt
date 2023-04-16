package com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.drawer.content.folder.DrawerFolders
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenus
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.clothes.ClothesCategoryType
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemState
import com.leebeebeom.clothinghelper.ui.theme.Black11
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf

@Composable // skippable
fun DrawerMainMenus(
    mainMenus: ImmutableList<MainMenu>,
    onMainMenuClick: (MainMenuType) -> Unit,
    onSubMenuClick: (SubMenuType) -> Unit,
    onClothesCategoryClick: (ClothesCategoryType) -> Unit,
    onFolderClick: (Folder) -> Unit,
    folders: (parentKey: String) -> ImmutableList<Folder>,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    editFolder: EditFolder,
    deleteFolder: (Folder) -> Unit
) {
    @Composable
    fun Folders(parentKey: String, backgroundColor: Color, basePadding: Dp) {
        DrawerFolders(
            parentKey = parentKey,
            folders = folders,
            folderNames = folderNames,
            backgroundColor = backgroundColor,
            foldersSize = foldersSize,
            itemsSize = { 0 },
            onFolderClick = onFolderClick,
            addFolder = addFolder,
            editFolder = editFolder,
            deleteFolder = deleteFolder,
            basePadding = basePadding
        )
    }

    Column {
        mainMenus.forEach { mainMenu ->
            key(mainMenu.type) {
                if (mainMenu.type != MainMenuType.Archive) {
                    val state = rememberDrawerItemState()

                    DrawerMainMenu(
                        mainMenu = mainMenu,
                        onMainMenuClick = onMainMenuClick,
                        state = state,
                        subMenus = { mainMenuType ->
                            SubMenus(
                                mainMenuType = mainMenuType,
                                onSubMenuClick = onSubMenuClick,
                                onClothesCategoryClick = onClothesCategoryClick,
                                foldersSize = foldersSize,
                                folderNames = folderNames,
                                itemsSize = { 0 },
                                addFolder = addFolder,
                                folders = { parentKey, backgroundColor, basePadding ->
                                    Folders(parentKey, backgroundColor, basePadding)
                                }
                            )
                        }
                    )
                } else {
                    val state = rememberDrawerItemDropdownMenuState()

                    DrawerArchive(
                        state = state,
                        onClick = onMainMenuClick,
                        folderNames = folderNames,
                        foldersSize = foldersSize,
                        itemsSize = itemsSize,
                        addFolder = addFolder,
                        folders = {
                            Folders(
                                parentKey = MainMenuType.Archive.name,
                                backgroundColor = Black11,
                                basePadding = 0.dp
                            )
                        },
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
        MainMenu(R.string.clothes_cap, MainMenuType.Clothes),
        MainMenu(R.string.outfit_cap, MainMenuType.Outfit),
        MainMenu(R.string.archive_cap, MainMenuType.Archive)
    )
}
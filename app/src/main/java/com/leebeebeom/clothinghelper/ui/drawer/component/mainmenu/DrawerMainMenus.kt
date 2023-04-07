package com.leebeebeom.clothinghelper.ui.drawer.component.mainmenu

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf

@Composable // skippable
fun DrawerMainMenus(
    mainMenus: ImmutableList<MainMenu>,
    onMainMenuClick: (MainMenuType) -> Unit,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    subMenus: @Composable (MainMenuType) -> Unit,
    archiveFolders: @Composable () -> Unit
) {
    Column {
        mainMenus.forEach { mainMenu ->
            key(mainMenu.type) {
                if (mainMenu.type != MainMenuType.Archive)
                    DrawerMainMenu(
                        mainMenu = mainMenu,
                        onMainMenuClick = onMainMenuClick,
                        subMenus = subMenus
                    )
                else DrawerArchive(
                    onClick = onMainMenuClick,
                    folderNames = folderNames,
                    foldersSize = foldersSize,
                    itemsSize = itemsSize,
                    addFolder = addFolder,
                    archiveFolders = archiveFolders,
                )
            }
        }
    }
}

data class MainMenu(
    @StringRes val name: Int,
    val type: MainMenuType
)

enum class MainMenuType {
    Brand, Clothe, Outfit, Archive, Top, Bottom, Outer, Etc
}

@Composable
fun rememberMainMenus() = remember {
    persistentListOf(
        MainMenu(R.string.brand_cap, MainMenuType.Brand),
        MainMenu(R.string.clothes_cap, MainMenuType.Clothe),
        MainMenu(R.string.outfit_cap, MainMenuType.Outfit),
        MainMenu(R.string.archive_cap, MainMenuType.Archive)
    )
}
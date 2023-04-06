package com.leebeebeom.clothinghelper.ui.drawer.component.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsColumn
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf

@Composable // skippable
fun DrawerMainMenus(
    mainMenus: ImmutableList<MainMenu>,
    onMainMenuClick: (MainMenuType) -> Unit,
    archiveFolderNames: () -> ImmutableSet<String>,
    archiveFoldersSize: () -> Int,
    addFolder: AddFolder,
    subMenus: @Composable (MainMenuType) -> Unit,
    archiveFolders: @Composable () -> Unit
) {
    DrawerItemsColumn {
        mainMenus.forEach { mainMenu ->
            key(mainMenu.type) {
                if (mainMenu.type != MainMenuType.Archive)
                    DrawerMainMenu(
                        mainMenu = mainMenu,
                        onMainMenuClick = onMainMenuClick,
                        subMenus = subMenus
                    )
                else DrawerArchive(
                    archive = mainMenu,
                    onArchiveClick = onMainMenuClick,
                    archiveFoldersSize = archiveFoldersSize,
                    archiveFolderNames = archiveFolderNames,
                    archiveItemsSize = { 0 },
                    addFolder = addFolder,
                    archiveFolders = archiveFolders,
                )
            }
        }
    }
}

data class MainMenu(
    val name: Int, val type: MainMenuType
)

enum class MainMenuType {
    Brand, Clothe, Outfit, Archive, Top, Bottom, Outer, Etc
}

fun getMainMenus() = persistentListOf(
    MainMenu(R.string.brand_cap, MainMenuType.Brand),
    MainMenu(R.string.clothes_cap, MainMenuType.Clothe),
    MainMenu(R.string.outfit_cap, MainMenuType.Outfit),
    MainMenu(R.string.archive_cap, MainMenuType.Archive)
)
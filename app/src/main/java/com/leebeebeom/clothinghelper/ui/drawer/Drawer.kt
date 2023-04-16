package com.leebeebeom.clothinghelper.ui.drawer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.ui.component.BackHandlerWrapper
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerHeader
import com.leebeebeom.clothinghelper.ui.drawer.content.DrawerEssentialMenus
import com.leebeebeom.clothinghelper.ui.drawer.content.DrawerMenus
import com.leebeebeom.clothinghelper.ui.drawer.content.EssentialMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.DrawerMainMenus
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.MainMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.rememberMainMenus
import com.leebeebeom.clothinghelper.ui.drawer.content.rememberEssentialMenus
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.clothes.ClothesCategoryType
import kotlinx.coroutines.launch

@Composable // skippable
fun Drawer(
    onSettingIconClick: () -> Unit,
    onEssentialMenuClick: (essentialMenu: EssentialMenuType) -> Unit,
    onMainMenuClick: (MainMenuType) -> Unit,
    onSubMenuClick: (SubMenuType) -> Unit,
    onClothesCategoryClick: (ClothesCategoryType) -> Unit,
    onFolderClick: (Folder) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val essentialMenus = rememberEssentialMenus()
    val mainMenus = rememberMainMenus()
    val viewModel = hiltViewModel<DrawerViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val closeDrawer: () -> Unit = remember(
        key1 = coroutineScope,
        key2 = scaffoldState,
    ) { { coroutineScope.launch { scaffoldState.drawerState.close() } } }
    val drawerEnabled by remember { derivedStateOf { uiState.user != null } }

    CloseDrawerWhenUserSignOut(user = { uiState.user }, closeDrawer = closeDrawer)

    Scaffold(scaffoldState = scaffoldState,
        drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
        drawerGesturesEnabled = drawerEnabled,
        drawerBackgroundColor = MaterialTheme.colors.primary,
        drawerContent = {
            CenterDotProgressIndicator(
                backGround = MaterialTheme.colors.primary,
                show = { uiState.isLoading })

            DrawerHeader(userName = { uiState.user?.name },
                userEmail = { uiState.user?.email },
                onSettingIconClick = {
                    closeDrawer()
                    onSettingIconClick()
                })

            DrawerMenus {
                DrawerEssentialMenus(
                    onEssentialMenuClick = {
                        closeDrawer()
                        onEssentialMenuClick(it)
                    }, essentialMenus = essentialMenus
                )
                DrawerMainMenus(
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
                    folders = uiState::getFolders,
                    folderNames = uiState::getFolderNames,
                    foldersSize = uiState::getFoldersSize,
                    itemsSize = { 0 },
                    addFolder = viewModel::addFolder,
                    editFolder = viewModel::editFolderName
                )
            }
        }) {
        content(it)
        BackHandlerWrapper(enabled = { scaffoldState.drawerState.isOpen }, task = closeDrawer)
    }
}

@Composable
fun CloseDrawerWhenUserSignOut( // skippable
    user: () -> User?, closeDrawer: () -> Unit
) {
    val localUser by remember(user) { derivedStateOf(user) }
    val currentCloseDrawer by rememberUpdatedState(newValue = closeDrawer)

    LaunchedEffect(key1 = user) {
        if (localUser == null) currentCloseDrawer()
    }
}
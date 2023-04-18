package com.leebeebeom.clothinghelper.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.MenuType
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.drawer.Drawer
import com.leebeebeom.clothinghelper.ui.drawer.content.EssentialMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.MainMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.clothes.ClothesCategoryType
import com.leebeebeom.clothinghelper.ui.main.MainGraphRoute
import com.leebeebeom.clothinghelper.ui.main.mainGraph
import com.leebeebeom.clothinghelper.ui.main.menu.brand.BrandGraphRoute
import com.leebeebeom.clothinghelper.ui.main.menu.clothes.ClothesGraphRoute
import com.leebeebeom.clothinghelper.ui.main.menu.clothes.closet.ClosetGraphRoute
import com.leebeebeom.clothinghelper.ui.main.menu.clothes.wish.WishGraphRoute
import com.leebeebeom.clothinghelper.ui.main.menu.outfit.OutfitGraphRoute
import com.leebeebeom.clothinghelper.ui.setting.SettingGraphRoute
import com.leebeebeom.clothinghelper.ui.setting.settingGraph
import com.leebeebeom.clothinghelper.ui.signin.ui.signInGraph
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.util.ParentKeyRoute
import com.leebeebeom.clothinghelper.ui.util.getCurrentRoute
import com.leebeebeom.clothinghelper.ui.viewmodel.ParentKey
import dagger.hilt.android.AndroidEntryPoint

const val Tag = "태그"

object MainNavRoute {
    const val SignInGraph = "sign in graph"
    const val MainGraph = "main graph"
    const val SettingGraph = "setting graph"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainNavHost() }
    }
}

@Composable // skippable
fun MainNavHost() {
    val viewModel = hiltViewModel<MainNavViewModel>()
    val navController = rememberNavController()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val startDestination by remember {
        derivedStateOf { uiState.user?.let { MainNavRoute.MainGraph } ?: MainNavRoute.SignInGraph }
    }

    ClothingHelperTheme {
        Drawer(
            onSettingIconClick = navController::navigateToSetting,
            onEssentialMenuClick = navController::onEssentialMenuClick,
            onMainMenuClick = navController::onMainMenuClick,
            onSubMenuClick = navController::onSubMenuClick,
            onClothesCategoryClick = navController::onClothesCategoryClick,
            onFolderClick = navController::onFolderClick
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = startDestination
            ) {
                signInGraph(navController = navController)
                settingGraph(navController = navController)
                mainGraph(
                    navController = navController,
                    onMainMenuClick = navController::onMainMenuClick
                )
            }
        }
        CenterDotProgressIndicator(
            show = { uiState.isLoading }, backGround = MaterialTheme.colors.background
        )
    }
}

private fun NavHostController.navigateToSetting() {
    navigateOnce(SettingGraphRoute.SettingScreen)
}

private fun NavHostController.onEssentialMenuClick(type: EssentialMenuType) {
    when (type) {
        EssentialMenuType.MainScreen -> navigateOnce(route = MainGraphRoute.MainScreen)
        EssentialMenuType.Favorite -> navigateOnce(route = MainGraphRoute.FavoriteScreen)
        EssentialMenuType.SeeAll -> navigateOnce(route = MainGraphRoute.SeeAllScreen)
        EssentialMenuType.Trash -> navigateOnce(route = MainGraphRoute.TrashScreen)
    }
}

private fun NavHostController.onMainMenuClick(type: MainMenuType) {
    when (type) {
        MainMenuType.Brand -> navigateOnce(route = BrandGraphRoute.BrandMainScreen)
        MainMenuType.Clothes -> navigateOnce(route = ClothesGraphRoute.ClothesScreen)
        MainMenuType.Outfit -> navigateOnce(route = OutfitGraphRoute.OutfitScreen)
        MainMenuType.Archive -> navigateOnce(
            route = MainGraphRoute.ArchiveScreen,
            parentKey = MainMenuType.Archive.name
        )
    }
}

private fun NavHostController.onSubMenuClick(type: SubMenuType) {
    when (type) {
        SubMenuType.Brand -> navigateOnce(
            route = BrandGraphRoute.BrandScreen,
            parentKey = SubMenuType.Brand.name
        )

        SubMenuType.Shop -> navigateOnce(
            route = BrandGraphRoute.ShopScreen,
            parentKey = SubMenuType.Shop.name
        )

        SubMenuType.Closet -> navigateOnce(route = ClosetGraphRoute.ClosetScreen)
        SubMenuType.Wish -> navigateOnce(route = WishGraphRoute.WishScreen)
        SubMenuType.Ootd -> navigateOnce(
            route = OutfitGraphRoute.OotdScreen,
            parentKey = SubMenuType.Ootd.name
        )

        SubMenuType.Reference -> navigateOnce(
            route = OutfitGraphRoute.ReferenceScreen,
            parentKey = SubMenuType.Reference.name
        )
    }
}

private fun NavHostController.onClothesCategoryClick(type: ClothesCategoryType) {
    if (currentBackStackEntry?.arguments?.getString(ParentKey) != type.name) {
        when (type) {
            is ClothesCategoryType.Closet ->
                navigateOnce(
                    route = ClosetGraphRoute.ClosetDetailScreen,
                    parentKey = type.name
                )

            is ClothesCategoryType.Wish ->
                navigateOnce(
                    route = WishGraphRoute.WishDetailScreen,
                    parentKey = type.name
                )
        }
    }
}

private fun NavHostController.onFolderClick(folder: Folder) {
    if (currentBackStackEntry?.arguments?.getString(ParentKey) != folder.key)
        when (folder.menuType) {
            MenuType.Brand -> navigateOnce(
                route = BrandGraphRoute.BrandScreen,
                parentKey = folder.key
            )

            MenuType.Shop -> navigateOnce(
                route = BrandGraphRoute.ShopScreen,
                parentKey = folder.key
            )

            MenuType.ClosetDetail -> navigateOnce(
                route = ClosetGraphRoute.ClosetDetailScreen,
                parentKey = folder.key
            )

            MenuType.WishDetail -> navigateOnce(
                route = WishGraphRoute.WishDetailScreen,
                parentKey = folder.key
            )

            MenuType.Ootd -> navigateOnce(
                route = OutfitGraphRoute.OotdScreen,
                parentKey = folder.key
            )

            MenuType.Reference -> navigateOnce(
                route = OutfitGraphRoute.ReferenceScreen,
                parentKey = folder.key
            )

            MenuType.Archive -> navigateOnce(
                route = MainGraphRoute.ArchiveScreen,
                parentKey = folder.key
            )
        }
}

private fun NavHostController.navigateOnce(route: String) {
    if (currentBackStackEntry.getCurrentRoute() != route) navigate(route = route)
}

private fun NavHostController.navigateOnce(route: ParentKeyRoute, parentKey: String) {
    if (currentBackStackEntry?.arguments?.getString(ParentKey) != parentKey)
        navigate(route = route.getRouteWithParentKeyRoute(parentKey = parentKey))
}
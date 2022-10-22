package com.leebeebeom.clothinghelper.main.base

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leebeebeom.clothinghelper.main.maincategory.MainCategoryScreen
import com.leebeebeom.clothinghelper.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryScreen
import com.leebeebeom.clothinghelperdomain.model.BaseMenuIds.FAVORITE
import com.leebeebeom.clothinghelperdomain.model.BaseMenuIds.MAIN_SCREEN
import com.leebeebeom.clothinghelperdomain.model.BaseMenuIds.SEE_ALL
import com.leebeebeom.clothinghelperdomain.model.BaseMenuIds.TRASH

sealed class MainDestinations(val route: String) {
    object MainCategory : MainDestinations("mainCategory")
    object SubCategory : MainDestinations("subCategory") {
        const val parentId: String = "id"
        val routeWithArg = "$route/{$parentId}"
        val arguments = listOf(navArgument(parentId) { type = NavType.IntType })
    }

    object Setting : MainDestinations("setting")
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    MainScreenRoot(onSettingIconClick = { navController.mainNavigate(MainDestinations.Setting.route) },
        onDrawerContentClick = { id -> navController.drawerNavigate(id) }) {
        NavHost(
            navController = navController,
            startDestination = MainDestinations.MainCategory.route,
            modifier = Modifier.padding(it)
        ) {
            composable(MainDestinations.MainCategory.route) {
                MainCategoryScreen(
                    onMainCategoryClick = { id -> navController.navigateToSubCategory(id) })
            }
            composable(
                route = MainDestinations.SubCategory.routeWithArg,
                arguments = MainDestinations.SubCategory.arguments
            ) { entry ->
                val parentId = entry.arguments?.getInt(MainDestinations.SubCategory.parentId) ?: -1
                SubCategoryScreen(parentId)
            }
            composable(MainDestinations.Setting.route) {
                SettingScreen()
            }
        }
    }
}

fun NavController.mainNavigate(destination: String) = navigate(destination) {
    popUpTo(MainDestinations.MainCategory.route)
    launchSingleTop = true
}

fun NavController.navigateToSubCategory(argument: Int) {
    mainNavigate("${MainDestinations.SubCategory.route}/$argument")
}

fun NavController.drawerNavigate(id: Int) {
    when (id) {
        MAIN_SCREEN -> mainNavigate(MainDestinations.MainCategory.route)
        FAVORITE -> {/*TODO*/
        }
        SEE_ALL -> { /*TODO*/
        }
        TRASH -> { /*TODO*/
        }
        else -> navigateToSubCategory(id)
    }
}
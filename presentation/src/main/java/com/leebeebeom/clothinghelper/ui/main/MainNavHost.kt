package com.leebeebeom.clothinghelper.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.data.model.BaseMenu.BOTTOM
import com.leebeebeom.clothinghelper.data.model.BaseMenu.ETC
import com.leebeebeom.clothinghelper.data.model.BaseMenu.FAVORITE
import com.leebeebeom.clothinghelper.data.model.BaseMenu.MAIN_SCREEN
import com.leebeebeom.clothinghelper.data.model.BaseMenu.OUTER
import com.leebeebeom.clothinghelper.data.model.BaseMenu.SEE_ALL
import com.leebeebeom.clothinghelper.data.model.BaseMenu.TOP
import com.leebeebeom.clothinghelper.data.model.BaseMenu.TRASH
import com.leebeebeom.clothinghelper.ui.main.base.MainScreenRoot
import com.leebeebeom.clothinghelper.ui.main.maincategory.MainCategoryScreen
import com.leebeebeom.clothinghelper.ui.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.ui.main.subcategory.SubCategoryScreen

object MainDestinations {
    const val MAIN_CATEGORY = "mainCategory"
    const val SUB_CATEGORY = "subCategory"
    const val SETTING = "setting"
}

@Composable
fun MainNavHost(name: String, email: String) {
    val navController = rememberNavController()

    MainScreenRoot(
        onSettingIconClick = { navController.mainNavigate(MainDestinations.SETTING) },
        onDrawerContentClick = { id -> navController.drawerNavigate(id) },
        name = name, email = email
    ) {
        NavHost(
            navController = navController,
            startDestination = MainDestinations.MAIN_CATEGORY,
            modifier = Modifier.padding(it)
        ) {
            composable(MainDestinations.MAIN_CATEGORY) {
                MainCategoryScreen(
                    onMainCategoryClick = { navController.mainNavigate(MainDestinations.SUB_CATEGORY) })
            }
            composable(MainDestinations.SUB_CATEGORY) { SubCategoryScreen() }
            composable(MainDestinations.SETTING) { SettingScreen() }
        }
    }
}

fun NavController.mainNavigate(destination: String) =
    navigate(destination) {
        popUpTo(MainDestinations.MAIN_CATEGORY)
    }

fun NavController.drawerNavigate(id: Int) {
    when (id) {
        MAIN_SCREEN -> mainNavigate(MainDestinations.MAIN_CATEGORY)
        FAVORITE -> {/*TODO*/
        }
        SEE_ALL -> { /*TODO*/
        }
        TRASH -> { /*TODO*/
        }
        TOP -> mainNavigate(MainDestinations.SUB_CATEGORY) //TODO
        BOTTOM -> mainNavigate(MainDestinations.SUB_CATEGORY) //TODO
        OUTER -> mainNavigate(MainDestinations.SUB_CATEGORY) //TODO
        ETC -> mainNavigate(MainDestinations.SUB_CATEGORY) //TODO
        else -> mainNavigate(MainDestinations.SUB_CATEGORY) //TODO
    }
}
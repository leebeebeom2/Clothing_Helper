package com.leebeebeom.clothinghelper.ui.main.maincategory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.main.maincategory.MainNavigationRoute.*
import com.leebeebeom.clothinghelper.ui.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.ui.main.subCategory.SubCategoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityNavHost() }
    }
}

enum class MainNavigationRoute(val route: String) {
    MAIN_CATEGORY("mainCategory"),
    SUB_CATEGORY("subCategory"),
    SETTING("setting")
}

@Composable
fun MainActivityNavHost() {
    fun mainNavigate(navController: NavController, destination: MainNavigationRoute) {
        navController.navigate(destination.route) {
            popUpTo(MAIN_CATEGORY.route)
        }
    }

    val navController = rememberNavController()

    MainActivityRoot(onNavigationSetting = { mainNavigate(navController, SETTING) }) {
        NavHost(
            navController = navController,
            startDestination = MAIN_CATEGORY.route,
            modifier = Modifier.padding(it)
        ) {
            composable(MAIN_CATEGORY.route) {
                HomeScreen(onNavigateToSubCategory = { mainNavigate(navController, SUB_CATEGORY) })
            }
            composable(SUB_CATEGORY.route) { SubCategoryScreen() }
            composable(SETTING.route) { SettingScreen() }
        }
    }
}
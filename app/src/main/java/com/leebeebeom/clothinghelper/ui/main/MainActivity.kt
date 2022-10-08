package com.leebeebeom.clothinghelper.ui.main

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
import com.leebeebeom.clothinghelper.ui.main.maincategory.HomeScreen
import com.leebeebeom.clothinghelper.ui.main.maincategory.MainActivityRoot
import com.leebeebeom.clothinghelper.ui.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.ui.main.subCategory.SubCategoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityNavHost() }
    }
}

object MainDestinations {
    const val MAIN_CATEGORY = "mainCategory"
    const val SUB_CATEGORY = "subCategory"
    const val SETTING = "setting"
}

@Composable
fun MainActivityNavHost() {
    fun mainNavigate(navController: NavController, destination: String) {
        navController.navigate(destination) {
            popUpTo(MainDestinations.MAIN_CATEGORY)
        }
    }

    val navController = rememberNavController()

    MainActivityRoot(onNavigationSetting = { mainNavigate(navController, MainDestinations.SETTING) }) {
        NavHost(
            navController = navController,
            startDestination = MainDestinations.MAIN_CATEGORY,
            modifier = Modifier.padding(it)
        ) {
            composable(MainDestinations.MAIN_CATEGORY) {
                HomeScreen(onNavigateToSubCategory = { mainNavigate(navController, MainDestinations.SUB_CATEGORY) })
            }
            composable(MainDestinations.SUB_CATEGORY) { SubCategoryScreen() }
            composable(MainDestinations.SETTING) { SettingScreen() }
        }
    }
}
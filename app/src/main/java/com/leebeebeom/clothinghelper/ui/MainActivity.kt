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
import com.leebeebeom.clothinghelper.ui.component.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.ui.drawer.Drawer
import com.leebeebeom.clothinghelper.ui.drawer.content.EssentialMenuType
import com.leebeebeom.clothinghelper.ui.main.MainGraphRoute
import com.leebeebeom.clothinghelper.ui.main.mainGraph
import com.leebeebeom.clothinghelper.ui.main.navigateToSubCategory
import com.leebeebeom.clothinghelper.ui.setting.SettingGraphRoute
import com.leebeebeom.clothinghelper.ui.setting.settingGraph
import com.leebeebeom.clothinghelper.ui.signin.ui.signInGraph
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.util.getCurrentRoute
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "태그"

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
fun MainNavHost(
    viewModel: MainNavViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val startDestination by remember {
        derivedStateOf {
            uiState.user?.let { MainNavRoute.MainGraph } ?: MainNavRoute.SignInGraph
        }
    }

    fun onEssentialMenuClick(type: EssentialMenuType) =
        when (type) {
            EssentialMenuType.MainScreen -> navController.navigateToMain()
            EssentialMenuType.Favorite -> {} // TODO
            EssentialMenuType.SeeAll -> {} // TODO
            EssentialMenuType.Trash -> {} // TODO
        }

    ClothingHelperTheme {
        Drawer(
            onSettingIconClick = navController::navigateToSetting,
            onEssentialMenuClick = ::onEssentialMenuClick,
            onMainMenuClick = navController::navigateToSubCategory,
            onSubMenuClick = {},
            onClothesCategoryClick = {},
            onFolderClick = {}
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = startDestination
            ) {
                mainGraph(navController = navController)
                settingGraph(navController = navController)
                signInGraph(navController = navController)
            }
        }
        CenterDotProgressIndicator(
            show = { uiState.isLoading },
            backGround = MaterialTheme.colors.background
        )
    }
}

private fun NavHostController.navigateToMain() {
    if (currentBackStackEntry.getCurrentRoute() != MainGraphRoute.Dashboard.route)
        navigate(route = MainNavRoute.MainGraph)
}

private fun NavHostController.navigateToSetting() {
    if (currentBackStackEntry.getCurrentRoute() != SettingGraphRoute.Setting.route)
        navigate(route = MainNavRoute.SettingGraph)
}
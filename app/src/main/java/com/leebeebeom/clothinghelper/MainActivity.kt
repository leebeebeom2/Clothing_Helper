package com.leebeebeom.clothinghelper

import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.MainNavigationRoute.MAIN_CATEGORY
import com.leebeebeom.clothinghelper.MainNavigationRoute.SETTING
import com.leebeebeom.clothinghelper.MainNavigationRoute.SUB_CATEGORY
import com.leebeebeom.clothinghelper.ui.main.HomeScreen
import com.leebeebeom.clothinghelper.ui.main.setting.SettingScreen
import com.leebeebeom.clothinghelper.ui.main.subCategory.SubCategoryScreen
import com.leebeebeom.clothinghelper.ui.signin.FirebaseExecutor
import com.leebeebeom.clothinghelper.ui.signin.SignInActivity

class MainActivity : ComponentActivity() {
    private val authStateListener = FirebaseAuth.AuthStateListener {
        if (it.currentUser == null) startSignInActivity()
        else FirebaseExecutor.reLoadUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityNavHost() }
    }

    override fun onStart() {
        super.onStart()
        FirebaseExecutor.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseExecutor.removeAuthStateListener(authStateListener)
    }

    private fun startSignInActivity() =
        startActivity(Intent(this, SignInActivity::class.java))
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
            composable(SETTING.route) {
                SettingScreen(onNavigateToMain = { mainNavigate(navController, MAIN_CATEGORY) })
            }
        }
    }
}
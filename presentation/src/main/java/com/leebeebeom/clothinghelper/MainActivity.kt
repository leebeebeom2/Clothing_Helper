package com.leebeebeom.clothinghelper

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.main.MainNavHost
import com.leebeebeom.clothinghelper.signin.SignInNavHost
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetSignInStateUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val TAG = "TAG"

@HiltAndroidApp
class ClothingHelper : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityNavHost() }
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MainActivityNavHost(
    viewModel: MainActivityViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val isSignIn by viewModel.getSignInStateUseCase().collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = MainActivityDestinations.MAIN) {
        composable(route = MainActivityDestinations.MAIN) { MainNavHost() }
        composable(route = MainActivityDestinations.SIGN_IN) { SignInNavHost() }
    }

    if (isSignIn) navController.mainActivityNavigate(MainActivityDestinations.MAIN)
    else navController.mainActivityNavigate(MainActivityDestinations.SIGN_IN)
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(val getSignInStateUseCase: GetSignInStateUseCase) :
    ViewModel()

object MainActivityDestinations {
    const val MAIN = "main"
    const val SIGN_IN = "signIn"
}

private fun NavHostController.mainActivityNavigate(route: String) = navigate(route) {
    launchSingleTop = true
}
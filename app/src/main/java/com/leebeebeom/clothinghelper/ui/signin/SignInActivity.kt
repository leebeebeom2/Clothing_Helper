package com.leebeebeom.clothinghelper.ui.signin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.ThemeRoot
import com.leebeebeom.clothinghelper.ui.signin.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.signup.SignUpScreen

object SignInNavigationRoute {
    const val SIGN_IN = "signIn"
    const val SIGN_UP = "signUp"
    const val RESET_PASSWORD = "resetPassword"
}

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SignInNavHost() }
    }
}

@Composable
fun SignInNavHost() {
    val navController = rememberNavController()

    ThemeRoot {
        Scaffold {
            NavHost(
                navController = navController,
                startDestination = SignInNavigationRoute.SIGN_IN,
                modifier = Modifier.padding(it)
            ) {
                composable(SignInNavigationRoute.SIGN_IN) { SignInScreen(navController) }
                composable(SignInNavigationRoute.SIGN_UP) { SignUpScreen() }
                composable(SignInNavigationRoute.RESET_PASSWORD) { ResetPasswordScreen(navController) }
            }
        }
    }
}
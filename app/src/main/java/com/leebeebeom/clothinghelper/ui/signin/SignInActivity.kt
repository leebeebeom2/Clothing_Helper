package com.leebeebeom.clothinghelper.ui.signin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.signin.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.signup.SignUpScreen

const val SIGN_IN = "signIn"
const val SIGN_UP = "signUp"
const val RESET_PASSWORD = "resetPassword"

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SignInNavHost() }
    }
}

@Composable
fun SignInNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SIGN_IN) {
        composable(SIGN_IN) { SignInScreen(navController) }
        composable(SIGN_UP) { SignUpScreen() }
        composable(RESET_PASSWORD) { ResetPasswordScreen(navController) }
    }
}
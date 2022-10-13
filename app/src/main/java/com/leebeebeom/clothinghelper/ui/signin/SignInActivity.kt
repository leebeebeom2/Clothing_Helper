package com.leebeebeom.clothinghelper.ui.signin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.signin.base.SignInActivityRoot
import com.leebeebeom.clothinghelper.ui.signin.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.signup.SignUpScreen

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SignInNavHost() }
    }
}

object SignInDestinations {
    const val SIGN_IN = "signIn"
    const val SIGN_UP = "signUp"
    const val RESET_PASSWORD = "resetPassword"
}

@Composable
fun SignInNavHost() {
    val navController = rememberNavController()

    fun signNavigate(destination: String) {
        navController.navigate(destination) {
            popUpTo(SignInDestinations.SIGN_IN)
        }
    }

    SignInActivityRoot {
        NavHost(
            navController = navController,
            startDestination = SignInDestinations.SIGN_IN,
            modifier = Modifier.padding(it)
        ) {
            composable(SignInDestinations.SIGN_IN) {
                SignInScreen(
                    onForgotPasswordClick = { signNavigate(SignInDestinations.RESET_PASSWORD) },
                    onEmailSignUpClick = { signNavigate(SignInDestinations.SIGN_UP) }
                )
            }
            composable(SignInDestinations.SIGN_UP) { SignUpScreen() }
            composable(SignInDestinations.RESET_PASSWORD) {
                ResetPasswordScreen()
            }
        }
    }
}

object FirebaseErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
}
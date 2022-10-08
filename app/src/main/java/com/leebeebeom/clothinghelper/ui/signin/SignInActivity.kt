package com.leebeebeom.clothinghelper.ui.signin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leebeebeom.clothinghelper.ui.ThemeRoot
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
    fun signNavigate(navController: NavController, destination: String) {
        navController.navigate(destination) {
            popUpTo(SignInDestinations.SIGN_IN)
        }
    }

    val navController = rememberNavController()

    ThemeRoot {
        Scaffold {
            NavHost(
                navController = navController,
                startDestination = SignInDestinations.SIGN_IN,
                modifier = Modifier.padding(it)
            ) {
                composable(SignInDestinations.SIGN_IN) {
                    SignInScreen(
                        onNavigateToResetPassword = {
                            signNavigate(
                                navController,
                                SignInDestinations.RESET_PASSWORD
                            )
                        },
                        onNavigateToSignUp = {
                            signNavigate(
                                navController,
                                SignInDestinations.SIGN_UP
                            )
                        }
                    )
                }
                composable(SignInDestinations.SIGN_UP) { SignUpScreen() }
                composable(SignInDestinations.RESET_PASSWORD) {
                    val context = LocalContext.current
                    ResetPasswordScreen(popBackStack = { navController.popBackStack() })
                }
            }
        }
    }
}
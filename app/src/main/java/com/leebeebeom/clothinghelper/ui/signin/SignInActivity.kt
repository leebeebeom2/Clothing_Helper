package com.leebeebeom.clothinghelper.ui.signin

import android.os.Bundle
import android.widget.Toast
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
import com.leebeebeom.clothinghelper.ui.signin.SignInNavigationRoute.*
import com.leebeebeom.clothinghelper.ui.signin.resetpassword.ResetPasswordScreen
import com.leebeebeom.clothinghelper.ui.signin.signin.SignInScreen
import com.leebeebeom.clothinghelper.ui.signin.signup.SignUpScreen

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SignInNavHost() }
    }
}

enum class SignInNavigationRoute(val route: String) {
    SIGN_IN("signIn"),
    SIGN_UP("signUp"),
    RESET_PASSWORD("resetPassword")
}

@Composable
fun SignInNavHost() {
    fun signNavigate(navController: NavController, destination: SignInNavigationRoute) {
        navController.navigate(destination.route) {
            popUpTo(SIGN_IN.route)
        }
    }

    val navController = rememberNavController()

    ThemeRoot {
        Scaffold {
            NavHost(
                navController = navController,
                startDestination = SIGN_IN.route,
                modifier = Modifier.padding(it)
            ) {
                composable(SIGN_IN.route) {
                    SignInScreen(
                        onNavigateToResetPassword = { signNavigate(navController, RESET_PASSWORD) },
                        onNavigateToSignUp = { signNavigate(navController, SIGN_UP) }
                    )
                }
                composable(SIGN_UP.route) { SignUpScreen() }
                composable(RESET_PASSWORD.route) {
                    val context = LocalContext.current
                    ResetPasswordScreen(popBackStack = {
                        Toast.makeText(context, "팝스택 실행", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    })
                }
            }
        }
    }
}
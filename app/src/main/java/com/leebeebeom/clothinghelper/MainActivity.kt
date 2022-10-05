package com.leebeebeom.clothinghelper

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.MainActivityNavigationRoute.MAIN
import com.leebeebeom.clothinghelper.MainActivityNavigationRoute.SUB_TYPE
import com.leebeebeom.clothinghelper.ui.main.HomeScreen
import com.leebeebeom.clothinghelper.ui.signin.SignInActivity
import com.leebeebeom.clothinghelper.ui.main.subtype.SubTypeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityNavHost() }
    }

    override fun onResume() {
        super.onResume()
        if (FirebaseAuth.getInstance().currentUser == null)
            startActivity(Intent(this, SignInActivity::class.java))
    }
}

object MainActivityNavigationRoute {
    const val MAIN = "main"
    const val SUB_TYPE = "subType"
}

@Composable
fun MainActivityNavHost() {
    val navController = rememberNavController()

    MainActivityRoot {
        NavHost(
            navController = navController,
            startDestination = MAIN,
            modifier = Modifier.padding(it)
        ) {
            composable(MAIN) { HomeScreen(navController) }
            composable(SUB_TYPE) { SubTypeScreen() }
        }
    }
}
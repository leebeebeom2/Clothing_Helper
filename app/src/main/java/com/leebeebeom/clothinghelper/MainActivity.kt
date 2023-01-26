package com.leebeebeom.clothinghelper

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.domain.usecase.user.GetSignInStateUseCase
import com.leebeebeom.clothinghelper.ui.main.MainNavHost
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavHost
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltAndroidApp
class ClothingHelper : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityScreen() }
    }
}

@Composable
fun MainActivityScreen(viewModel: MainActivityViewModel = hiltViewModel()) {
    val isSignIn by viewModel.isSignIn.collectAsStateWithLifecycle()

    Crossfade(targetState = isSignIn) { if (it) MainNavHost() else SignInNavHost() }
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val getSignInStateUseCase: GetSignInStateUseCase) :
    ViewModel() {
    val isSignIn get() = getSignInStateUseCase.isSignIn
}
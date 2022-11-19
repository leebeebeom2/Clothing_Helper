package com.leebeebeom.clothinghelper

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.main.MainNavHost
import com.leebeebeom.clothinghelper.signin.SignInNavHost
import com.leebeebeom.clothinghelperdomain.usecase.user.GetSignInStateUseCase
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

@OptIn(ExperimentalLifecycleComposeApi::class)
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
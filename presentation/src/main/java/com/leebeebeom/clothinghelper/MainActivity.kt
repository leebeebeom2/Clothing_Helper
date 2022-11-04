package com.leebeebeom.clothinghelper

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.base.Anime.Screen.slideInBottom
import com.leebeebeom.clothinghelper.base.Anime.Screen.slideOutBottom
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
        setContent { MainActivityScreen() }
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MainActivityScreen(viewModel: MainActivityViewModel = hiltViewModel()) {
    val isSignIn by viewModel.getSignInStateUseCase().collectAsStateWithLifecycle()

    Box {
        MainNavHost()
        AnimatedVisibility(
            visible = !isSignIn,
            enter = slideInBottom,
            exit = slideOutBottom
        ) { SignInNavHost() }
    }
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(val getSignInStateUseCase: GetSignInStateUseCase) :
    ViewModel()
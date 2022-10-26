package com.leebeebeom.clothinghelper

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.main.base.MainNavHost
import com.leebeebeom.clothinghelper.signin.SignInNavHost
import com.leebeebeom.clothinghelperdomain.usecase.user.GetSignInStateUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

@Composable
fun MainActivityNavHost(viewModel: MainActivityViewModel = hiltViewModel()) {
    // TODO
    // 사인인 스크린 밑으로 내려가게 변경

    Crossfade(targetState = viewModel.isSignIn, animationSpec = tween(500)) {
        if (it) MainNavHost()
        else SignInNavHost()
    }
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getSignInStateUseCase: GetSignInStateUseCase
) : ViewModel() {

    var isSignIn by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            getSignInStateUseCase().collect { isSignIn = it }
        }
    }
}
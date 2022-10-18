package com.leebeebeom.clothinghelper.ui

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.ui.main.MainNavHost
import com.leebeebeom.clothinghelper.ui.signin.SignInNavHost
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

const val TAG = "태그"

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
fun MainActivityNavHost(viewModel: MainActivityViewModel = viewModel()) {
    val viewModelState = viewModel.viewModelState

    if (viewModelState.isLogin) MainNavHost(viewModelState.name, viewModelState.email)
    else SignInNavHost()
}
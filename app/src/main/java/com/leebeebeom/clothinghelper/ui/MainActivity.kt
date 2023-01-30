package com.leebeebeom.clothinghelper.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.ui.components.ToastWrapper
import com.leebeebeom.clothinghelper.ui.main.MainNavHost
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityScreen() }
    }

    @Composable
    private fun MainActivityScreen(
        viewModel: ActivityViewModel = hiltViewModel(),
        uiState: ActivityUiState = viewModel.activityUiState
    ) {
        val isSignIn by viewModel.isSignIn.collectAsStateWithLifecycle()

        CrossFadeWrapper { isSignIn }

        ToastWrapper(
            text = { uiState.toastText },
            toastShown = viewModel::toastShown
        )
    }

    @Composable
    private fun CrossFadeWrapper(state: () -> Boolean) {
        Crossfade(targetState = state()) { if (it) MainNavHost() else SignInNavHost() }
    }
}
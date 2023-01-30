package com.leebeebeom.clothinghelper.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.ui.main.MainNavHost
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavHost
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityScreen() }
    }

    @Composable
    private fun MainActivityScreen(
        viewModel: ActivityViewModel = activityViewModel(),
        uiState: ActivityUiState = viewModel.activityUiState
    ) {
        val isSignIn by viewModel.isSignIn.collectAsStateWithLifecycle()

        ClothingHelperTheme { CrossFadeWrapper { isSignIn } }

        ToastWrapper(
            text = { uiState.toastText }, toastShown = viewModel::toastShown
        )
    }

    @Composable
    private fun CrossFadeWrapper(state: () -> Boolean) {
        Crossfade(targetState = state()) { if (it) MainNavHost() else SignInNavHost() }
    }

    @Composable
    private fun ToastWrapper(@StringRes text: () -> Int?, toastShown: () -> Unit) {
        text()?.let {
            Toast.makeText(LocalContext.current, stringResource(id = it), Toast.LENGTH_SHORT).show()
            toastShown()
        }
    }
}
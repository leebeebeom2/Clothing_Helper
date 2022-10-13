package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.ui.CenterCircularProgressIndicator
import com.leebeebeom.clothinghelper.ui.SimpleToast
import com.leebeebeom.clothinghelper.ui.signin.SignInViewModel
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme

@Composable
fun SignInBaseRoot(
    isLoading: Boolean,
    @StringRes toastText: Int?,
    toastShown: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
        ) { focusManager.clearFocus() }
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        content = content)

    toastText?.let {
        SimpleToast(text = it, shownToast = toastShown)
    }
    if (isLoading) CenterCircularProgressIndicator()
}

@Composable
fun SignInActivityRoot(
    viewModel: SignInViewModel = viewModel(), content: @Composable (PaddingValues) -> Unit
) {
    if (viewModel.isLogin) (LocalContext.current as ComponentActivity).finish()

    ClothingHelperTheme {
        Scaffold(content = content)
    }
}
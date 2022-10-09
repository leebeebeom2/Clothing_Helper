package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.ui.CenterCircularProgressIndicator
import com.leebeebeom.clothinghelper.ui.SimpleToast
import com.leebeebeom.clothinghelper.ui.ThemeRoot
import com.leebeebeom.clothinghelper.ui.signin.SignInViewModel

@Composable
fun SignInBaseRoot(
    isLoading: Boolean,
    toastTextId: Int?,
    toastShown: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        content = content
    )

    toastTextId?.let {
        SimpleToast(resId = it, shownToast = toastShown)
    }

    if (isLoading) CenterCircularProgressIndicator()
}

@Composable
fun SignInActivityRoot(
    viewModel: SignInViewModel = viewModel(),
    content: @Composable (PaddingValues) -> Unit
) {
    if (viewModel.isLogin) (LocalContext.current as ComponentActivity).finish()

    ThemeRoot {
        Scaffold(content = content)
    }
}
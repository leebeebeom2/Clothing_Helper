package com.leebeebeom.clothinghelper.signin.base

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.base.SimpleToast
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme

@Composable
fun SignInRoot(
    state: SignInRootState,
    toastShown: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    ClothingHelperTheme {
        Scaffold {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(it)
                .clickable(
                    interactionSource = interactionSource, indication = null
                ) { focusManager.clearFocus() }
                .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                content = content)
        }
    }

    state.toastText?.let { SimpleToast(text = it, shownToast = toastShown) }

    if (state.isLoading) CenterDotProgressIndicator()
}

data class SignInRootState(
    val isLoading: Boolean = false, @StringRes val toastText: Int? = null
)
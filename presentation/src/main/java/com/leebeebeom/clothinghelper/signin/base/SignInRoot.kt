package com.leebeebeom.clothinghelper.signin.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.base.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme

@Composable
fun SignInRoot(
    viewModel: SignInRootViewModel = hiltViewModel(),
    content: @Composable (PaddingValues) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    ClothingHelperTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .clickable(
                    interactionSource = interactionSource, indication = null
                ) { focusManager.clearFocus() }
                .verticalScroll(rememberScrollState()),
            content = content
        )
    }

    if (viewModel.isLoading) CenterDotProgressIndicator()
}
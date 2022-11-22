package com.leebeebeom.clothinghelper.signin.base.composables

import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.base.composables.MaxWidthButton
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer

@Composable
fun ButtonWithGoogleButton(
    @StringRes buttonText: Int,
    buttonEnabled: () -> Boolean,
    onButtonClick: () -> Unit,
    googleButtonEnabled: () -> Boolean,
    onActivityResult: (ActivityResult) -> Unit,
    googleButtonDisable: () -> Unit
) {
    MaxWidthButton(
        text = buttonText,
        enabled = buttonEnabled,
        onClick = onButtonClick
    )
    SimpleHeightSpacer(dp = 8)
    OrDivider()
    SimpleHeightSpacer(dp = 8)
    GoogleSignInButton(
        enabled = googleButtonEnabled,
        onActivityResult = onActivityResult,
        disabled = googleButtonDisable
    )
}
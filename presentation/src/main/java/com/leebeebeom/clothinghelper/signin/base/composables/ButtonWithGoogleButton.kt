package com.leebeebeom.clothinghelper.signin.base.composables

import androidx.activity.result.ActivityResult
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer

@Composable
fun ButtonWithGoogleButton(
    buttons: @Composable () -> Unit,
    googleButtonEnabled: () -> Boolean,
    onActivityResult: (ActivityResult) -> Unit,
    googleButtonDisable: () -> Unit
) {
    buttons()
    SimpleHeightSpacer(dp = 8)
    OrDivider()
    SimpleHeightSpacer(dp = 8)
    GoogleSignInButton(
        enabled = googleButtonEnabled,
        onActivityResult = onActivityResult,
        disabled = googleButtonDisable
    )
}
package com.leebeebeom.clothinghelper.ui.main.composables

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableList

@Composable // skippable
fun ToastWrapper(toastTexts: () -> ImmutableList<Int>, toastShown: () -> Unit) {
    toastTexts().firstOrNull()?.let {
        Toast.makeText(LocalContext.current, stringResource(id = it), Toast.LENGTH_SHORT).show()
        toastShown()
    }
}
package com.leebeebeom.clothinghelper.composable

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun BlockBacKPressWhenLoading(isLoading: () -> Boolean) {
    BackHandler(enabled = isLoading) {}
}

@Composable
fun BackHandler(enabled: () -> Boolean, task: () -> Unit) {
    BackHandler(enabled(), onBack = task)
}
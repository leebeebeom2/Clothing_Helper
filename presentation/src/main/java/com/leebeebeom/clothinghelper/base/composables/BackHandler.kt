package com.leebeebeom.clothinghelper.base.composables

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun BackHandler(enabled: () -> Boolean, task: () -> Unit) {
    BackHandler(enabled(), onBack = task)
}

@Composable
fun BlockBacKPressWhenIsLoading(isLoading: () -> Boolean) {
    BackHandler(enabled = isLoading) {}
}
package com.leebeebeom.clothinghelper.composable

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

@Composable
fun SelectModeBackHandler(
    isSelectMode: () -> Boolean, selectModeOff: (CoroutineScope) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = isSelectMode(), onBack = { selectModeOff(coroutineScope) })
}

@Composable
fun BlockBacKPressWhenLoading(isLoading: () -> Boolean) {
    BackHandler(enabled = isLoading) {}
}

@Composable
fun BackHandler(enabled: () -> Boolean, task: () -> Unit) {
    BackHandler(enabled(), onBack = task)
}
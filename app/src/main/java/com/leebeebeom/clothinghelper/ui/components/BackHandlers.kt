package com.leebeebeom.clothinghelper.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

@Composable
fun SelectModeBackHandler(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    isSelectMode: () -> Boolean,
    selectModeOff: (CoroutineScope) -> Unit
) {
    BackHandlerWrapper(enabled = isSelectMode, task = { selectModeOff(coroutineScope) })
}

@Composable
fun BlockBacKPressWhenLoading(isLoading: () -> Boolean) {
    BackHandlerWrapper(enabled = isLoading) {}
}

@Composable
fun BackHandlerWrapper(enabled: () -> Boolean, task: () -> Unit) {
    BackHandler(enabled(), onBack = task)
}
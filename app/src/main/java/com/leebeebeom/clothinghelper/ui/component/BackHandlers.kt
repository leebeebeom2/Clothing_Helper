package com.leebeebeom.clothinghelper.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SelectModeBackHandler(
    isSelectMode: () -> Boolean,
    selectModeOff: suspend () -> Unit
) {
    val scope = rememberCoroutineScope()

    BackHandlerWrapper(enabled = isSelectMode, task = { scope.launch { selectModeOff() } })
}

@Composable
fun BackHandlerWrapper(enabled: () -> Boolean, task: () -> Unit) {
    BackHandler(enabled = enabled(), onBack = task)
}
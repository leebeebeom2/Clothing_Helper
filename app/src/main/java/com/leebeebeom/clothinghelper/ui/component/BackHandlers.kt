package com.leebeebeom.clothinghelper.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
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
fun BackHandlerWrapper(enabled: () -> Boolean, task: () -> Unit) {
    val localEnabled by remember(enabled) { derivedStateOf(enabled) }

    BackHandler(localEnabled, onBack = task)
}
package com.leebeebeom.clothinghelper.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

@Composable
fun SelectModeBackHandler(
    isSelectMode: () -> Boolean,
    selectModeOff: (CoroutineScope) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    BackHandlerWrapper(enabled = isSelectMode, task = { selectModeOff(coroutineScope) })
}

@Composable
fun BackHandlerWrapper(enabled: () -> Boolean, task: () -> Unit) {
    val localEnabled by remember(enabled) { derivedStateOf(enabled) }

    BackHandler(localEnabled, onBack = task)
}
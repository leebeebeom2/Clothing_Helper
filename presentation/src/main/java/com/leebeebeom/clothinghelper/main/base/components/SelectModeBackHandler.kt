package com.leebeebeom.clothinghelper.main.base.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SelectModeBackHandler(
    isSelectMode: () -> Boolean,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    selectModeOff: suspend () -> Unit
) {
    BackHandler(enabled = isSelectMode(), onBack = {
        coroutineScope.launch { selectModeOff() }
    })
}
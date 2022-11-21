package com.leebeebeom.clothinghelper.main.base.composables

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SelectModeBackHandler(
    isSelectMode: () -> Boolean, selectModeOff: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = isSelectMode(), onBack = { coroutineScope.launch { selectModeOff() } })
}
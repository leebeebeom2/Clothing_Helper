package com.leebeebeom.clothinghelper.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
fun TestScope.backgroundLaunch(task: suspend () -> Unit) {
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { task() }
}
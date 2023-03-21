package com.leebeebeom.clothinghelper.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * wait [time] second
 */
suspend fun waitTime(time: Long = 1000L) =
    withContext(Dispatchers.Default) { delay(time) }
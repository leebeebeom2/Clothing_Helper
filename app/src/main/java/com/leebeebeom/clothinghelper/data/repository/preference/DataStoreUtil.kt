package com.leebeebeom.clothinghelper.data.repository.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import java.io.IOException

fun <T> DataStore<Preferences>.getFlow(
    scope: CoroutineScope,
    transForm: suspend (Preferences) -> T
) = data.catch {
    if (it is IOException) emit(emptyPreferences()) else throw it
}.map { transForm(it) }
    .shareIn(scope = scope, started = SharingStarted.WhileSubscribed(5000), 1)
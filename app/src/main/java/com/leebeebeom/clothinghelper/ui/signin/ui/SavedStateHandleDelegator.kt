package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SavedStateHandleDelegator<T : Any?>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String,
    private val initial: T
) : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>) =
        savedStateHandle.get<T>(key) ?: initial

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        savedStateHandle[key] = value
    }
}
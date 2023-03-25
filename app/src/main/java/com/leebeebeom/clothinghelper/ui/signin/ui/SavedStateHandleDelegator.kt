package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SavedStateHandleDelegator<T : Any>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String,
    private val initial: T
) : ReadWriteProperty<Any, T> {
    private lateinit var value: T
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (!::value.isInitialized)
            value = savedStateHandle.get<T>(key) ?: initial
        return value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        savedStateHandle[key] = value
        this.value = value
    }
}
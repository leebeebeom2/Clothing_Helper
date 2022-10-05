package com.leebeebeom.clothinghelper.util

import androidx.compose.runtime.MutableState
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StateDelegator<T>(private val state: MutableState<T>) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): T = state.value

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        state.value = value
    }
}

class FirebaseTaskStateDelegator(private val firebaseTaskState: MutableState<Boolean>) :
    ReadWriteProperty<SignInBaseViewModel, Boolean> {
    override fun getValue(thisRef: SignInBaseViewModel, property: KProperty<*>): Boolean {
        return if (firebaseTaskState.value) {
            firebaseTaskState.value = false
            true
        } else false
    }

    override fun setValue(thisRef: SignInBaseViewModel, property: KProperty<*>, value: Boolean) {
        firebaseTaskState.value = value
    }
}

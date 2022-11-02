package com.leebeebeom.clothinghelper.util

import kotlinx.coroutines.flow.MutableStateFlow

inline fun <T> Collection<T>.taskAndReturn(crossinline task: (MutableList<T>) -> Unit): Set<T> {
    val temp = toMutableList()
    task(temp)
    return temp.toSet()
}

fun <T> MutableStateFlow<T>.update(task: (T) -> T) {
    this.value = task(value)
}
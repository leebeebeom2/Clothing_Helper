package com.leebeebeom.clothinghelperdata.repository.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun <T> MutableStateFlow<List<T>>.updateMutable(task: (MutableList<T>) -> Unit) {
    val temp = value.toMutableList()
    task(temp)
    update { temp }
}
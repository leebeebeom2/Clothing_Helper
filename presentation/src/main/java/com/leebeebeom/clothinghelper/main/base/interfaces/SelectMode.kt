package com.leebeebeom.clothinghelper.main.base.interfaces

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.util.taskAndReturnSet
import com.leebeebeom.clothinghelperdomain.model.container.BaseContainer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.delay

interface SelectMode<T : BaseContainer> {
    val list: ImmutableList<T>
    val isSelectMode: Boolean
    val selectedKeys: ImmutableSet<String>
    val selectedSize: Int
    val isAllSelected: Boolean
    val firstSelectedItem : T?
    fun selectModeOn(key: String)
    fun onSelect(key: String)
    suspend fun selectModeOff()
    fun toggleAllSelect()
}

class SelectModeImpl<T : BaseContainer> : SelectMode<T> {
    override var list: ImmutableList<T> = emptyList<T>().toImmutableList()
    override var isSelectMode by mutableStateOf(false)
        private set

    override var selectedKeys by mutableStateOf(linkedSetOf<String>().toImmutableSet())
        private set

    override val selectedSize by derivedStateOf { selectedKeys.size }
    override val isAllSelected by derivedStateOf { selectedKeys.size == list.size }
    override val firstSelectedItem by derivedStateOf {
        selectedKeys.firstOrNull()
            ?.let { key -> list.firstOrNull { it.key == key } }
    }

    override fun selectModeOn(key: String) {
        onSelect(key)
        isSelectMode = true
    }

    override fun onSelect(key: String) {
        selectedKeys =
            if (selectedKeys.contains(key)) selectedKeys.taskAndReturnSet {
                it.remove(key)
            }
            else selectedKeys.taskAndReturnSet { it.add(key) }
    }

    override suspend fun selectModeOff() {
        isSelectMode = false
        delay(200)
        selectedKeys = selectedKeys.taskAndReturnSet { it.clear() }
    }

    override fun toggleAllSelect() {
        selectedKeys =
            if (selectedSize == list.size) emptySet<String>().toImmutableSet()
            else list.map { it.key }.toImmutableSet()
    }
}
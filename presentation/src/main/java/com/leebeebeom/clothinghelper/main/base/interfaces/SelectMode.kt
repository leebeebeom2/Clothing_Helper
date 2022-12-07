package com.leebeebeom.clothinghelper.main.base.interfaces

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.util.taskAndReturnSet
import com.leebeebeom.clothinghelperdomain.model.data.BaseModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.delay

interface SelectMode<T : BaseModel> {
    val items: ImmutableList<T>
    val isSelectMode: Boolean
    val selectedKeys: ImmutableSet<String>
    val selectedSize: Int
    val isAllSelected: Boolean
    val firstSelectedItem: T
    val showEditIcon: Boolean
    val showDeleteIcon: Boolean
    fun selectModeOn(key: String)
    fun onSelect(key: String)
    suspend fun selectModeOff()
    fun toggleAllSelect()
}

class SelectModeImpl<T : BaseModel> : SelectMode<T> {
    override val items: ImmutableList<T>
        get() = TODO("Not yet implemented")

    override var isSelectMode by mutableStateOf(false)
        private set

    override var selectedKeys by mutableStateOf(linkedSetOf<String>().toImmutableSet())
        private set

    override val selectedSize by derivedStateOf { selectedKeys.size }

    /**
     * derivedStateOf { selectedKeys.size == items.size }
     */
    override val isAllSelected get() = TODO("Not yet implemented")

    override val showEditIcon by derivedStateOf { selectedKeys.size == 1 }
    override val showDeleteIcon by derivedStateOf { selectedKeys.size > 0 }

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

    fun toggleAllSelect(items: List<BaseModel>) {
        selectedKeys =
            if (selectedSize == items.size) emptySet<String>().toImmutableSet()
            else items.map { it.key }.toImmutableSet()
    }

    /**
     * get() = items.first { it.key == selectedKeys.first() }
     */
    override val firstSelectedItem get() = TODO("Not yet implemented")

    /**
     * selectModeImpl.toggleAllSelect(list)
     */
    override fun toggleAllSelect() {
        TODO("Not yet implemented")
    }
}
package com.leebeebeom.clothinghelper.ui.main.interfaces

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelperdomain.model.data.BaseModel
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.delay

interface SelectMode<T : BaseModel> {
    val isSelectMode: Boolean
    val selectedKeys: ImmutableSet<String>
    val selectedSize: Int
    val firstSelectedItem: T
    fun selectModeOn(key: String)
    fun onSelect(key: String)
    fun onSelect(keys: List<String>)
    suspend fun selectModeOff()
    fun toggleAllSelect()
}

class SelectModeImpl<T : BaseModel> : SelectMode<T> {
    override var isSelectMode by mutableStateOf(false)
        private set

    override var selectedKeys by mutableStateOf(linkedSetOf<String>().toImmutableSet())
        private set

    override val selectedSize by derivedStateOf { selectedKeys.size }

    override fun selectModeOn(key: String) {
        onSelect(key)
        isSelectMode = true
    }

    override fun onSelect(keys: List<String>) = keys.forEach(::onSelect)

    override fun onSelect(key: String) {
        selectedKeys =
            if (selectedKeys.contains(key)) selectedKeys.taskAndReturn { it.remove(key) }
            else selectedKeys.taskAndReturn { it.add(key) }
    }

    override suspend fun selectModeOff() {
        isSelectMode = false
        delay(200)
        selectedKeys = selectedKeys.taskAndReturn { it.clear() }
    }

    fun toggleAllSelect(items: List<BaseModel>) {
        selectedKeys =
            if (selectedSize == items.size) emptySet<String>().toImmutableSet()
            else items.map { it.key }.toImmutableSet()
    }

    fun getFirstSelectedItem(items: List<T>) = items.first { it.key == selectedKeys.first() }

    /**
     * call selectModeImpl.getFirstSelectedItem()
     */
    override val firstSelectedItem get() = TODO("Not yet implemented")

    /**
     * call selectModeImpl.toggleAllSelect()
     */
    override fun toggleAllSelect() {
        TODO("Not yet implemented")
    }

    private inline fun <T> Collection<T>.taskAndReturn(crossinline task: (MutableSet<T>) -> Unit): ImmutableSet<T> {
        val temp = toMutableSet()
        task(temp)
        return temp.toImmutableSet()
    }
}
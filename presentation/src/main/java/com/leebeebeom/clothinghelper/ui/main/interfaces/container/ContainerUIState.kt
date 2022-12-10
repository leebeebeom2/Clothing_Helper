package com.leebeebeom.clothinghelper.ui.main.interfaces.container

import androidx.compose.runtime.*
import com.leebeebeom.clothinghelperdomain.model.data.BaseModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

interface ContainerUIState<T : BaseModel> {
    val allItems: List<T>
    val items: Map<String, State<ImmutableList<T>>>

    fun load(allItems: List<T>)
    fun getItems(key: String, predicate: (T) -> Boolean): ImmutableList<T>
}

class ContainerUIStateImpl<T : BaseModel> : ContainerUIState<T> {
    override var allItems: List<T> by mutableStateOf(emptyList())
        private set

    override var items by mutableStateOf(mapOf<String, State<ImmutableList<T>>>())
        private set

    override fun load(allItems: List<T>) {
        this.allItems = allItems
    }

    override fun getItems(key: String, predicate: (T) -> Boolean): ImmutableList<T> {
        if (items[key] == null) {
            val mutableMap = items.toMutableMap()

            mutableMap[key] = derivedStateOf {
                allItems.filter(predicate).toImmutableList()
            }
            items = mutableMap
        }
        return items[key]!!.value
    }
}
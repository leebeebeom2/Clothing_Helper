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

    override val items = hashMapOf<String, State<ImmutableList<T>>>()

    override fun load(allItems: List<T>) {
        this.allItems = allItems
    }

    override fun getItems(key: String, predicate: (T) -> Boolean): ImmutableList<T> {
        if (items[key] == null)
            items[key] = derivedStateOf {
                allItems.filter(predicate).toImmutableList()
            }
        return items[key]!!.value
    }
}
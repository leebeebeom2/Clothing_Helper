package com.leebeebeom.clothinghelper.domain.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey override val key: String = "",
    val text: String = "",
    val done: Boolean = false,
    val order: Int = 0,
    override val isSynced: Boolean = false,
) : BaseModel() {
    override fun addKey(key: String) = copy(key = key)

    override fun synced() = copy(isSynced = true)
}
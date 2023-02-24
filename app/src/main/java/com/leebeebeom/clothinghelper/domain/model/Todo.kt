package com.leebeebeom.clothinghelper.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Todo(
    override val key: String = "",
    val text: String = "",
    val done: Boolean = false,
    val order: Int = 0,
) : BaseModel()

@Entity
data class RoomTodo(
    @PrimaryKey override val key: String,
    val text: String = "",
    val done: Boolean = false,
    val order: Int = 0,
) : BaseRoomModel() {
    override fun addKey(key: String) = copy(key = key)
}

data class FirebaseTodo(
    override val key: String,
    val text: String = "",
    val done: Boolean = false,
    val order: Int = 0,
) : BaseFirebaseModel()
package com.leebeebeom.clothinghelper.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Todo(
    override val key: String = "",
    val text: String = "",
    val done: Boolean = false,
    val order: Int = 0,
) : BaseModel() {
    override fun addKey(key: String) = copy(key = key)
}

@Entity
data class DatabaseTodo(
    @PrimaryKey override val key: String,
    val text: String = "",
    val done: Boolean = false,
    val order: Int = 0,
    override val createDate: Long = 0,
    override val editDate: Long = 0,
) : BaseDatabaseModel() {
    override fun addCreateData(date: Long): BaseDatabaseModel = copy(createDate = date)

    override fun addEditDate() = copy(editDate = System.currentTimeMillis())
    override fun addKey(key: String) = copy(key = key)
}
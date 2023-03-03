package com.leebeebeom.clothinghelper.domain.model

data class Todo(
    override val key: String = "",
    val text: String = "",
    val done: Boolean = false,
    val order: Int = 0,
) : BaseModel() {
    override fun addKey(key: String) = copy(key = key)
}
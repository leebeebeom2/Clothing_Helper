package com.leebeebeom.clothinghelper.domain.model

data class ArchiveItem(override val key: String = "") : BaseModel() {
    override fun addKey(key: String) = copy(key = key)
}
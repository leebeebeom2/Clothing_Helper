package com.leebeebeom.clothinghelper.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class Brand(
    override val key: String = "",
    override val name: String = "",
    override val url: String = "",
    val stockist: ImmutableList<Shop> = persistentListOf()
) : BaseBrandModel() {
    override fun addKey(key: String) = copy(key = key)
}
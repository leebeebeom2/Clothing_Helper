package com.leebeebeom.clothinghelper.domain.model.data

import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

data class SizeChart(
    val isFavorite: Boolean = false,
    val brand: String = "",
    val sizes: ImmutableList<Int> = emptyList<Int>().toImmutableList(),
    val fields: ImmutableMap<String, String> = emptyMap<String, String>().toImmutableMap(),
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
    override val name: String = "",
    override val key: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0,
    override val parent: MainCategoryType = MainCategoryType.TOP,
    override val isSynced: Boolean = false,
) : BaseFolderModel() {
    override fun addKey(key: String) = copy(key = key)
}
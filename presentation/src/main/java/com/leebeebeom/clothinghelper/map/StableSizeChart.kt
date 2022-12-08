package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.data.BaseFolderModel
import com.leebeebeom.clothinghelperdomain.model.data.SizeChart
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

data class StableSizeChart(
    val isFavorite: Boolean = false,
    val brand: String = "",
    val sizes: List<Int> = emptyList(),
    val fields: Map<String, String> = emptyMap(),
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
    override val name: String = "",
    override val key: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0,
    override val parent: SubCategoryParent = SubCategoryParent.TOP
) : BaseFolderModel() {
    override fun addKey(key: String) = copy(key = key)
}

fun StableSizeChart.toUnstable() = SizeChart(
    parentKey = parentKey,
    subCategoryKey = subCategoryKey,
    isFavorite = isFavorite,
    brand = brand,
    sizes = sizes,
    name = name,
    key = key,
    createDate = createDate,
    editDate = editDate
)

fun SizeChart.toStable() = StableSizeChart(
    parentKey = parentKey,
    subCategoryKey = subCategoryKey,
    isFavorite = isFavorite,
    brand = brand,
    sizes = sizes,
    name = name,
    key = key,
    createDate = createDate,
    editDate = editDate
)
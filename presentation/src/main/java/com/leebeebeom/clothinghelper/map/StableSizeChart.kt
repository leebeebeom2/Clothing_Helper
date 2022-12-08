package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.data.BaseFolderModel
import com.leebeebeom.clothinghelperdomain.model.data.SizeChart
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

data class StableSizeChart(
    val isFavorite: Boolean = false,
    val link: String = "",
    val memo: String = "",
    val brand: String = "",
    val photoUrl: String = "",
    val sizes: List<Int> = emptyList(),
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
    link = link,
    memo = memo,
    brand = brand,
    photoUrl = photoUrl,
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
    link = link,
    memo = memo,
    brand = brand,
    photoUrl = photoUrl,
    sizes = sizes,
    name = name,
    key = key,
    createDate = createDate,
    editDate = editDate
)
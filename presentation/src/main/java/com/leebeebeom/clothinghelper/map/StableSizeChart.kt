package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.data.SizeChart
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

data class StableSizeChart(
    val subCategoryKey: String = "",
    val isFavorite: Boolean = false,
    val link: String = "",
    val memo: String = "",
    val brand: String = "",
    val photoUrl: String = "",
    val sizes: List<Int> = emptyList(),
    override val name: String = "",
    override val key: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0,
    override val parent: SubCategoryParent = SubCategoryParent.TOP
) : BaseStableModel()

fun StableSizeChart.toUnstable() =
    SizeChart(
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

fun SizeChart.toStable() =
    StableSizeChart(
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
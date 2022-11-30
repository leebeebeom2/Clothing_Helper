package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.SizeChart
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent

data class StableSizeChart(
    val parentKey: String = "",
    val subCategoryKey: String = "",
    val parent: SubCategoryParent = SubCategoryParent.TOP,
    val createTime: Long = 0,
    val editTime: Long = 0,
    val isFavorite: Boolean = false,
    val link: String = "",
    val memo: String = ""
)

fun StableSizeChart.toUnstable() =
    SizeChart(
        parentKey = parentKey,
        subCategoryKey = subCategoryKey,
        parent = parent,
        createTime = createTime,
        editTime = editTime,
        isFavorite = isFavorite,
        link = link,
        memo = memo
    )

fun SizeChart.toStable() =
    StableSizeChart(
        parentKey = parentKey,
        subCategoryKey = subCategoryKey,
        parent = parent,
        createTime = createTime,
        editTime = editTime,
        isFavorite = isFavorite,
        link = link,
        memo = memo
    )
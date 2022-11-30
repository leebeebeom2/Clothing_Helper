package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.data.SizeChart
import com.leebeebeom.clothinghelperdomain.model.data.BaseModel
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

data class StableSizeChart(
    val subCategoryKey: String = "",
    val isFavorite: Boolean = false,
    val link: String = "",
    val memo: String = "",
    override val name: String = "",
    override val key: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0,
    override val parent: SubCategoryParent = SubCategoryParent.TOP
) : BaseModel()

fun StableSizeChart.toUnstable() =
    SizeChart(
        subCategoryKey = subCategoryKey,
        isFavorite = isFavorite,
        link = link,
        memo = memo,
        name = name,
        key = key,
        createDate = createDate,
        editDate = editDate,
        parent = parent
    )

fun SizeChart.toStable() =
    StableSizeChart(
        subCategoryKey = subCategoryKey,
        isFavorite = isFavorite,
        link = link,
        memo = memo,
        name = name,
        key = key,
        createDate = createDate,
        editDate = editDate,
        parent = parent
    )
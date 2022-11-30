package com.leebeebeom.clothinghelperdomain.model

import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent

data class SizeChart(
    val parentKey: String = "",
    val subCategoryKey: String = "",
    val parent: SubCategoryParent = SubCategoryParent.TOP,
    val createTime: Long = 0,
    val editTime: Long = 0,
    val isFavorite: Boolean = false,
    val link: String = "",
    val memo: String = ""
)
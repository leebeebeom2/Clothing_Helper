package com.leebeebeom.clothinghelperdomain.model

import com.leebeebeom.clothinghelperdomain.model.container.BaseModel
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent

data class SizeChart(
    val subCategoryKey: String = "",
    val isFavorite: Boolean = false,
    val link: String = "",
    val memo: String = "",
    override val name: String = "",
    override val key: String = "",
    override val createDate: Long=0,
    override val editDate: Long=0,
    override val parent: SubCategoryParent = SubCategoryParent.TOP
) : BaseModel()
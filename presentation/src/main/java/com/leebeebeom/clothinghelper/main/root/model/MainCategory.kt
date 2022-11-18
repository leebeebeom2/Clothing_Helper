package com.leebeebeom.clothinghelper.main.root.model

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent
import kotlinx.collections.immutable.toImmutableList

data class MainCategory(val name: Int, val type: SubCategoryParent)

fun getMainCategories() = listOf(
    MainCategory(R.string.top, SubCategoryParent.TOP),
    MainCategory(R.string.bottom, SubCategoryParent.BOTTOM),
    MainCategory(R.string.outer, SubCategoryParent.OUTER),
    MainCategory(R.string.etc, SubCategoryParent.ETC)
).toImmutableList()

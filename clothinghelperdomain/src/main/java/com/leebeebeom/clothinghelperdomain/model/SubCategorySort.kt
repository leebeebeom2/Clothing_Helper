package com.leebeebeom.clothinghelperdomain.model

enum class SubCategorySort {
    NAME, CREATE
}

enum class SortOrder {
    ASCENDING, DESCENDING
}

data class SubCategorySortPreferences(
    val sort: SubCategorySort = SubCategorySort.NAME,
    val sortOrder: SortOrder = SortOrder.ASCENDING
)
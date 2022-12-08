package com.leebeebeom.clothinghelperdomain.model.data

data class SizeChart(
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
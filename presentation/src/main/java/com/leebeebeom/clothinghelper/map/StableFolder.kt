package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.container.BaseContainer
import com.leebeebeom.clothinghelperdomain.model.container.Folder
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent

data class StableFolder(
    val parentKey: String = "",
    val subCategoryKey: String = "",
    val subCategoryParent: SubCategoryParent = SubCategoryParent.TOP,
    override val createDate: Long = 0,
    override val name: String = "",
    override val key: String = "",
    override val editDate: Long = 0,
    override val parent: SubCategoryParent = SubCategoryParent.TOP
) : BaseContainer()

fun Folder.toStable() = StableFolder(
    parentKey = parentKey,
    subCategoryKey = subCategoryKey,
    subCategoryParent = subCategoryParent,
    createDate = createDate,
    name = name,
    key = key,
    editDate = editDate,
    parent = parent
)

fun StableFolder.toStable() = Folder(
    parentKey = parentKey,
    subCategoryKey = subCategoryKey,
    subCategoryParent = subCategoryParent,
    createDate = createDate,
    name = name,
    key = key,
    editDate = editDate,
    parent = parent
)
package com.leebeebeom.clothinghelperdomain.model.data

abstract class BaseModel {
    abstract val parent: SubCategoryParent
    abstract val name: String
    abstract val key: String
    abstract val createDate: Long
    abstract val editDate: Long

    abstract fun addKey(key: String): BaseModel
}

abstract class BaseFolderModel : BaseModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
}
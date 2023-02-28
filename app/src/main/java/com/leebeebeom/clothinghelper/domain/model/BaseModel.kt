package com.leebeebeom.clothinghelper.domain.model

abstract class BaseModel {
    abstract val key: String
    abstract fun addKey(key: String): BaseModel
}

abstract class BaseContainerModel : BaseModel() {
    abstract val name: String
}

abstract class BaseDatabaseContainerModel : BaseModel() {
    abstract val name: String
    abstract val createDate: Long
    abstract val editDate: Long
}

abstract class BaseFolderModel : BaseContainerModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
}

abstract class BaseDatabaseFolderModel : BaseDatabaseContainerModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
}
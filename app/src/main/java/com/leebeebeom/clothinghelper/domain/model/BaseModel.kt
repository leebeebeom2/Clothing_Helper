package com.leebeebeom.clothinghelper.domain.model

import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType

abstract class BaseModel {
    abstract val key: String
    abstract fun addKey(key: String): BaseModel
}

abstract class BaseDatabaseModel : BaseModel() {
    abstract val createDate: Long
    abstract val editDate: Long

    abstract fun addCreateData(date: Long = System.currentTimeMillis()): BaseDatabaseModel
    abstract fun addEditDate(): BaseDatabaseModel
}

abstract class BaseContainerModel : BaseModel() {
    abstract val name: String
}

abstract class BaseDatabaseContainerModel : BaseDatabaseModel() {
    abstract val name: String
}

abstract class BaseFolderModel : BaseContainerModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
    abstract val mainCategoryType: MainCategoryType
}

abstract class BaseDatabaseFolderModel : BaseDatabaseContainerModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
    abstract val mainCategoryType: MainCategoryType
}
package com.leebeebeom.clothinghelper.domain.model

abstract class BaseModel {
    abstract val key: String
}

abstract class BaseFirebaseModel : BaseModel()

abstract class BaseRoomModel : BaseFirebaseModel() {
    abstract fun addKey(key: String): BaseModel
}

abstract class BaseContainerModel : BaseModel() {
    abstract val name: String
}

abstract class BaseRoomContainerModel : BaseRoomModel() {
    abstract val name: String
    abstract val createDate: Long
    abstract val editDate: Long
}

abstract class BaseFirebaseContainerModel : BaseFirebaseModel() {
    abstract val name: String
    abstract val createDate: Long
    abstract val editDate: Long
}

abstract class BaseFolderModel : BaseContainerModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
}

abstract class BaseRoomFolderModel : BaseRoomContainerModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
}

abstract class BaseFirebaseFolderModel : BaseFirebaseContainerModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
}
package com.leebeebeom.clothinghelperdomain.model.container

abstract class BaseContainer {
    abstract val parent: SubCategoryParent
    abstract val name: String
    abstract val key: String
    abstract val createDate: Long
    abstract val editDate: Long
}
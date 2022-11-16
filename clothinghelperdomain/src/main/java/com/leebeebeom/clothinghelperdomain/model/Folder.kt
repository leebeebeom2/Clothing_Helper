package com.leebeebeom.clothinghelperdomain.model

data class Folder(val parentKey: String = "", val name: String = "", val key: String = "")

fun getDummyFolders() = List(100) { Folder(name = "이름") }
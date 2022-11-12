package com.leebeebeom.clothinghelperdomain.model

data class Folder(val parentKey: String = "", val name: String = "")

fun getDummyFolders() = List(10) { Folder(name = "이름") }
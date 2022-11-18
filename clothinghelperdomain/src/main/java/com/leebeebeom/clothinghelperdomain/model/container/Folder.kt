package com.leebeebeom.clothinghelperdomain.model.container

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(
    val parentKey: String = "",
    override val createDate: Long = 0,
    override val name: String = "",
    override val key: String = "",
    override val editDate: Long = 0
) : BaseContainer(), Parcelable

fun getDummyFolders() = List(100) { Folder(name = "이름") }
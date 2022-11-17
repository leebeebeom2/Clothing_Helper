package com.leebeebeom.clothinghelperdomain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(val parentKey: String = "", val name: String = "", val key: String = "") :
    Parcelable

fun getDummyFolders() = List(100) { Folder(name = "이름") }
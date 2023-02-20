package com.leebeebeom.clothinghelper.domain.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String = "",
    val name: String = "",
    val uid: String = ""
) : Parcelable
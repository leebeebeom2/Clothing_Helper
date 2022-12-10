package com.leebeebeom.clothinghelper.ui.main.interfaces

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.map.StableUser
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.data.User

interface UserUIState {
    val user: StableUser?

    fun updateUser(user: User?)
}

class UserUIStateImpl : UserUIState {
    override var user: StableUser? by mutableStateOf(null)
        private set

    override fun updateUser(user: User?) {
        this.user = user?.toStable()
    }
}
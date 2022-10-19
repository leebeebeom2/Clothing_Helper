package com.leebeebeom.clothinghelper.ui.main.setting

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SettingViewModel : ViewModel() {
    val signOut = { FirebaseAuth.getInstance().signOut() }
}
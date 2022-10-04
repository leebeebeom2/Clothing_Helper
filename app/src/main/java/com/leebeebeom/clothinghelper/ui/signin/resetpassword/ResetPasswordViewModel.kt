package com.leebeebeom.clothinghelper.ui.signin.resetpassword

import com.leebeebeom.clothinghelper.data.OutlinedTextFieldAttrFactory
import com.leebeebeom.clothinghelper.ui.signin.CHFirebase
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class ResetPasswordViewModel : SignInBaseViewModel() {
    override val emailTextFieldAttr by lazy { OutlinedTextFieldAttrFactory.resetPasswordEmail() }

    override val isTextFieldEmpty get() = emailTextFieldAttr.isEmpty
    override val isErrorEnable get() = emailTextFieldAttr.isErrorEnabled

    override fun firebaseTask(chFirebase: CHFirebase) =
        chFirebase.sendResetPasswordEmail(emailTextFieldAttr.text)
}
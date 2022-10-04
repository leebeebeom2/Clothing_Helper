package com.leebeebeom.clothinghelper.ui.signin

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.OutlinedTextFieldAttr
import com.leebeebeom.clothinghelper.data.StateDelegator
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.ERROR_USER_NOT_FOUND

object FirebaseErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
}

abstract class SignInBaseViewModel : ViewModel() {
    abstract val emailTextFieldAttr: OutlinedTextFieldAttr

    abstract val isTextFieldEmpty: Boolean
    abstract val isErrorEnable: Boolean
    val firebaseButtonEnable get() = !isTextFieldEmpty && !isErrorEnable

    private val _progressionOn = mutableStateOf(false)
    var progressionOn by StateDelegator(_progressionOn)
    private val _isFirebaseTaskSuccessful = mutableStateOf(false)
    var isFirebaseTaskSuccessful: Boolean
        get() {
            return if (_isFirebaseTaskSuccessful.value) {
                _isFirebaseTaskSuccessful.value = false
                true
            } else false
        }
        set(value) {
            _isFirebaseTaskSuccessful.value = value
        }

    val onFirebaseButtonClick = {
        progressionOn = true
        firebaseTask()
    }

    val onCompleteListener = { task: Task<*> ->
        if (task.isSuccessful) isFirebaseTaskSuccessful = true else taskFailed(task)
        progressionOn = false
    }

    private fun taskFailed(task: Task<*>) {
        (task.exception as? FirebaseAuthException)?.errorCode.let {
            when (it) {
                ERROR_INVALID_EMAIL -> emailTextFieldAttr.errorEnable(R.string.error_invalid_email)
                ERROR_EMAIL_ALREADY_IN_USE -> emailTextFieldAttr.errorEnable(R.string.error_email_already_in_use)
                ERROR_USER_NOT_FOUND -> emailTextFieldAttr.errorEnable(R.string.error_user_not_found)
            }
        }
    }

    protected abstract fun firebaseTask(chFirebase: CHFirebase = CHFirebase(onCompleteListener))
}
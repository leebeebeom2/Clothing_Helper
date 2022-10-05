package com.leebeebeom.clothinghelper.ui.signin

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.TextFieldAttr
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.ERROR_EMAIL_ALREADY_IN_USE
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.ERROR_INVALID_EMAIL
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.ERROR_USER_NOT_FOUND
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.ERROR_WRONG_PASSWORD
import com.leebeebeom.clothinghelper.util.FirebaseTaskStateDelegator
import com.leebeebeom.clothinghelper.util.StateDelegator

object FirebaseErrorCode {
    const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
    const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
}

abstract class SignInBaseViewModel : ViewModel() {
    protected val textFieldManager = TextFieldAttr.TextFieldManager()
    abstract val emailTextFieldAttr: TextFieldAttr
    abstract val passwordTextFieldAttr: TextFieldAttr?

    val firebaseButtonEnable get() = !textFieldManager.anyFieldEmpty && !textFieldManager.anyFieldErrorEnable

    private val _progressionOn = mutableStateOf(false)
    var progressionOn by StateDelegator(_progressionOn)

    private val _isFirebaseTaskSuccessful = mutableStateOf(false)
    var isFirebaseTaskSuccessful: Boolean by FirebaseTaskStateDelegator(_isFirebaseTaskSuccessful)

    private val _isFirebaseTaskFailed = mutableStateOf(false)
    var isFirebaseTaskFailed: Boolean by FirebaseTaskStateDelegator(_isFirebaseTaskFailed)

    val onFirebaseButtonClick = {
        progressionOn = true
        firebaseTask()
    }

    open val onCompleteListener = { task: Task<*> ->
        if (task.isSuccessful) isFirebaseTaskSuccessful = true else taskFailed(task)
        progressionOn = false
    }

    protected fun taskFailed(task: Task<*>) {
        (task.exception as? FirebaseAuthException)?.errorCode.let {
            when (it) {
                ERROR_INVALID_EMAIL -> emailTextFieldAttr.errorEnable(R.string.error_invalid_email)
                ERROR_EMAIL_ALREADY_IN_USE -> emailTextFieldAttr.errorEnable(R.string.error_email_already_in_use)
                ERROR_USER_NOT_FOUND -> emailTextFieldAttr.errorEnable(R.string.error_user_not_found)
                ERROR_WRONG_PASSWORD -> passwordTextFieldAttr?.errorEnable(R.string.error_wrong_password)
                else -> {
                    Log.d("TAG", "SignInBox: $it")
                    isFirebaseTaskFailed = true
                }
            }
        }
    }

    protected abstract fun firebaseTask(
        firebaseExecution: FirebaseExecution = FirebaseExecution(
            onCompleteListener
        )
    )
}
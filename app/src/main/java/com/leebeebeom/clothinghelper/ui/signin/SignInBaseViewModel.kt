package com.leebeebeom.clothinghelper.ui.signin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.data.TextFieldState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode.*

enum class FirebaseErrorCode(val errorCode: String) {
    ERROR_INVALID_EMAIL("ERROR_INVALID_EMAIL"),
    ERROR_USER_NOT_FOUND("ERROR_USER_NOT_FOUND"),
    ERROR_EMAIL_ALREADY_IN_USE("ERROR_EMAIL_ALREADY_IN_USE"),
    ERROR_WRONG_PASSWORD("ERROR_WRONG_PASSWORD")
}

abstract class SignInBaseViewModel : ViewModel() {
    protected val essentialTextFields = TextFieldState.EssentialTextFields()
    val emailTextFieldState by mutableStateOf(TextFieldState(essentialTextFields))
    abstract val passwordTextFieldState: TextFieldState?

    val firebaseButtonEnabled get() = !essentialTextFields.anyFieldBlank && !essentialTextFields.anyFieldErrorEnable

    var progressOn by mutableStateOf(false)
    val progressOff = {
        progressOn = false
    }

    var isFirebaseTaskSuccessful by mutableStateOf(false)

    var isFirebaseTaskFailed by mutableStateOf(false)

    val onFirebaseButtonClick = {
        progressOn = true
        firebaseTask()
    }

    protected fun taskFailed(task: Task<*>) {
        (task.exception as? FirebaseAuthException)?.errorCode?.let {
            when (it) {
                ERROR_INVALID_EMAIL.errorCode -> emailTextFieldState.errorEnable(TextFieldState.TextFieldError.ERROR_INVALID_EMAIL)
                ERROR_EMAIL_ALREADY_IN_USE.errorCode -> emailTextFieldState.errorEnable(
                    TextFieldState.TextFieldError.ERROR_EMAIL_ALREADY_IN_USE
                )
                ERROR_USER_NOT_FOUND.errorCode -> emailTextFieldState.errorEnable(TextFieldState.TextFieldError.ERROR_USER_NOT_FOUND)
                ERROR_WRONG_PASSWORD.errorCode -> passwordTextFieldState?.errorEnable(TextFieldState.TextFieldError.ERROR_WRONG_PASSWORD)
                else -> {
                    Log.d("TAG", "firebase taskFailed: $it")
                    isFirebaseTaskFailed = true
                }
            }
        }
    }

    /**
     * onFirebaseButtonClick 안에서 동작
     */
    protected abstract fun firebaseTask(
        firebaseUseCase: FirebaseUseCase = FirebaseUseCase(onCompleteListener)
    )

    open val onCompleteListener = { task: Task<*> ->
        if (task.isSuccessful)
            isFirebaseTaskSuccessful = true
        else taskFailed(task)
        progressOff()
    }
}
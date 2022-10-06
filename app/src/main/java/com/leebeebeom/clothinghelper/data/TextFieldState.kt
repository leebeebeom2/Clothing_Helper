package com.leebeebeom.clothinghelper.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.leebeebeom.clothinghelper.R

class TextFieldState(
    essentialTextFields: EssentialTextFields? = null,
    visibleIconEnable: Boolean = false
) {
    init {
        essentialTextFields?.addState(this)
    }

    var text by mutableStateOf("")

    val isBlank get() = text.isBlank()

    fun textInit() {
        text = ""
    }

    var error by mutableStateOf(TextFieldError.ERROR_OFF)

    val isErrorEnabled get() = error != TextFieldError.ERROR_OFF

    fun errorEnable(error: TextFieldError) {
        this.error = error
    }

    fun errorDisable() {
        error = TextFieldError.ERROR_OFF
    }

    var onValueChange: (String) -> Unit = ::onValueChange

    fun onValueChange(newText: String) {
        text = newText
        if (isErrorEnabled) errorDisable()
    }

    var visualTransformation by mutableStateOf(
        if (!visibleIconEnable) VisualTransformation.None else PasswordVisualTransformation()
    )

    val visibleToggle = {
        visualTransformation =
            if (isVisible) PasswordVisualTransformation() else VisualTransformation.None
    }

    private val isVisible get() = visualTransformation == VisualTransformation.None

    enum class TextFieldError(val resId: Int) {
        ERROR_OFF(R.string.empty),
        ERROR_WEAK_PASSWORD(R.string.error_weak_password),
        ERROR_PASSWORD_CONFIRM_NOT_SAME(R.string.error_password_confirm_not_same),
        ERROR_INVALID_EMAIL(R.string.error_invalid_email),
        ERROR_USER_NOT_FOUND(R.string.error_user_not_found),
        ERROR_EMAIL_ALREADY_IN_USE(R.string.error_email_already_in_use),
        ERROR_WRONG_PASSWORD(R.string.error_wrong_password),
        ERROR_SAME_CATEGORY_NAME(R.string.error_same_category_name)
    }

    class EssentialTextFields {
        private val states by lazy { mutableListOf<TextFieldState>() }

        val anyFieldBlank get() = states.any { it.isBlank }
        val anyFieldErrorEnable get() = states.any { it.isErrorEnabled }

        fun addState(state: TextFieldState) = states.add(state)
    }
}
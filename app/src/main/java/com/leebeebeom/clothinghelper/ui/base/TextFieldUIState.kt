package com.leebeebeom.clothinghelper.ui.base

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.leebeebeom.clothinghelper.R

data class TextFieldUIState(
    val text: String = "",
    val error: TextFieldError = TextFieldError.ERROR_OFF,
    val visualTransformation: VisualTransformation = VisualTransformation.None,
    val imeAction: ImeAction = ImeAction.Done,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = keyboardType,
        imeAction = imeAction
    )
) {
    val isBlank get() = text.isBlank()
    val isError get() = error != TextFieldError.ERROR_OFF

    fun textChangeAndErrorOff(newText: String) = copy(text = newText).errorOff()
    fun errorOff() = copy(error = TextFieldError.ERROR_OFF)
    fun errorOn(error: TextFieldError) = copy(error = error)
    fun textInit() = copy(text = "")
    fun setInvisible() = copy(visualTransformation = PasswordVisualTransformation())
    fun setVisible() = copy(visualTransformation = VisualTransformation.None)
}

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
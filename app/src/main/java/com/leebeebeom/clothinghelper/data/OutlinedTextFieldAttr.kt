package com.leebeebeom.clothinghelper.data

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.leebeebeom.clothinghelper.ui.SimpleIcon
import com.leebeebeom.clothinghelper.R
import kotlin.reflect.KProperty

data class OutlinedTextFieldAttr(
    val labelId: Int,
    private val _visualTransformation: MutableState<VisualTransformation> =
        mutableStateOf(VisualTransformation.None),
    val placeHolderId: Int = R.string.empty,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    private val visibleIconEnable: Boolean = false
) {
    companion object {
        private const val ERROR_OFF = -1

        fun email() = OutlinedTextFieldAttr(
            labelId = R.string.email,
            placeHolderId = R.string.example_email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        fun password() = OutlinedTextFieldAttr(
            labelId = R.string.password,
            _visualTransformation = mutableStateOf(PasswordVisualTransformation()),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visibleIconEnable = true
        )

        fun signUpPassword() =
            password().copy(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

        fun signUpName() = OutlinedTextFieldAttr(
            labelId = R.string.name,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        fun signUpPasswordConfirm() = password().copy(labelId = R.string.password_confirm)

        fun resetPasswordEmail() = email().copy(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )
    }

    private val _text = mutableStateOf("")
    var text by StateDelegator(_text)

    val isEmpty get() = text.isBlank()

    private val _errorTextId = mutableStateOf(-1)
    var errorTextId by StateDelegator(_errorTextId)
        private set

    var visualTransformation by StateDelegator(_visualTransformation)
        private set

    private val isVisible get() = visualTransformation == VisualTransformation.None

    var onValueChange = { newText: String ->
        text = newText
        if (isErrorEnabled) errorDisable()
    }

    val isErrorEnabled get() = errorTextId != ERROR_OFF

    fun errorEnable(resId: Int) {
        errorTextId = resId
    }

    fun errorDisable() {
        errorTextId = ERROR_OFF
    }

    val visibleIcon = @Composable {
        if (visibleIconEnable)
            IconButton(onClick = { visibleToggle() }) {
                if (isVisible) SimpleIcon(drawableId = R.drawable.ic_eye_close)
                else SimpleIcon(drawableId = R.drawable.ic_eye_open)
            }
    }

    private fun visibleToggle() {
        visualTransformation =
            if (isVisible) PasswordVisualTransformation() else VisualTransformation.None
    }
}

class StateDelegator<T>(private val state: MutableState<T>) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): T = state.value

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        state.value = value
    }
}
package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.UserRepository
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInImpl
import com.leebeebeom.clothinghelper.ui.signin.SignInBaseViewModel

class SignUpViewModel : SignInBaseViewModel(), GoogleSignInImpl {
    var signUpState by mutableStateOf(SignUpUIState())
        private set

    val onEmailChange = { newEmail: String ->
        signUpState = signUpState.onEmailChange(newEmail)
    }

    val onNameChange = { newName: String ->
        signUpState = signUpState.onNameChange(newName)
    }

    val onPasswordChange = { newPassword: String ->
        signUpState = signUpState.onPasswordChange(newPassword)

        if (newPassword.isNotBlank()) {
            passwordSameCheck()
            if (newPassword.length < 6) setPasswordWeakError()
        } else passwordErrorOff()
    }

    val onPasswordConfirmChange = { newPasswordConfirm: String ->
        signUpState = signUpState.onPasswordConfirmChange(newPasswordConfirm)

        passwordSameCheck()
    }

    private fun passwordSameCheck() {
        val passwordConfirmState = signUpState.passwordConfirmState
        val passwordSate = signUpState.passwordState

        if (!passwordConfirmState.isBlank)
            if (passwordSate.text != passwordConfirmState.text)
                setPasswordConfirmNotSameError()
            else passwordConfirmErrorOff()
        else passwordConfirmErrorOff()
    }

    fun signUpWithEmailAndPassword() {
        loadingOn()

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(
                signUpState.emailState.text,
                signUpState.passwordState.text
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(R.string.sign_up_complete)
                    if (it.result.user == null) showToast(R.string.name_update_failed)
                    else userNameUpdate(it.result.user!!)
                } else fireBaseErrorCheck(it.exception)
            }
    }

    private fun userNameUpdate(user: FirebaseUser) {
        val request = userProfileChangeRequest { displayName = signUpState.nameState.text }

        user.updateProfile(request).addOnCompleteListener {
            if (it.isSuccessful) UserRepository.userNameUpdate(signUpState.nameState.text)
            else showToast(R.string.name_update_failed)
            loadingOff()
        }
    }

    private fun setPasswordWeakError() {
        signUpState = signUpState.setPasswordError(TextFieldError.ERROR_WEAK_PASSWORD)
    }

    private fun passwordErrorOff() {
        signUpState = signUpState.passwordErrorOff()
    }

    private fun setPasswordConfirmNotSameError() {
        signUpState =
            signUpState.setPasswordConFirmError(TextFieldError.ERROR_PASSWORD_CONFIRM_NOT_SAME)
    }

    private fun passwordConfirmErrorOff() {
        signUpState = signUpState.passwordConfirmErrorOff()
    }

    val passwordVisualTransformationToggle = { isVisible: Boolean ->
        signUpState = signUpState.passwordVisualTransformationToggle(isVisible)
    }
    val passwordConfirmVisualTransformationToggle = { isVisible: Boolean ->
        signUpState = signUpState.passwordConfirmVisualTransformationToggle(isVisible)
    }

    private fun setEmailError(error: TextFieldError) {
        signUpState = signUpState.setEmailError(error)
    }

    private fun loadingOn() {
        signUpState = signUpState.loadingOn()
    }

    private fun loadingOff() {
        signUpState = signUpState.loadingOff()
    }

    override fun showToast(@StringRes toastText: Int) {
        signUpState = signUpState.showToast(toastText = toastText)
    }

    override fun setError(errorCode: String) {
        when (errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> setEmailError(TextFieldError.ERROR_INVALID_EMAIL)
            FirebaseErrorCode.ERROR_EMAIL_ALREADY_IN_USE -> setEmailError(TextFieldError.ERROR_EMAIL_ALREADY_IN_USE)
            else -> showToast(R.string.unknown_error)
        }
        loadingOff()
    }

    val toastShown = {
        signUpState = signUpState.toastShown()
    }

    override fun googleSignInTaskFinished(@StringRes toastText: Int) {
        showToast(toastText)
        loadingOff()
        setGoogleButtonEnabled(true)
    }

    override fun googleSignInTaskStart() {
        loadingOn()
        setGoogleButtonEnabled(false)
    }

    private fun setGoogleButtonEnabled(enabled: Boolean) {
        signUpState = signUpState.setGoogleButtonEnabled(enabled)
    }
}

data class SignUpUIState(
    val emailState: TextFieldUIState = TextFieldUIState(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Email
    ),
    val nameState: TextFieldUIState = TextFieldUIState(imeAction = ImeAction.Next),
    val passwordState: TextFieldUIState = TextFieldUIState(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Password,
        visualTransformation = PasswordVisualTransformation()
    ),
    val passwordConfirmState: TextFieldUIState = TextFieldUIState(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Password,
        visualTransformation = PasswordVisualTransformation()
    ),
    val isLoading: Boolean = false,
    @StringRes val toastText: Int? = null,
    val googleButtonEnabled: Boolean = true,
) {
    val signUpButtonEnabled
        get() = !emailState.isBlank && !emailState.isError &&
                !nameState.isBlank && !nameState.isError &&
                !passwordState.isBlank && !passwordState.isError &&
                !passwordConfirmState.isBlank && !passwordConfirmState.isError

    fun onEmailChange(newEmail: String) =
        copy(emailState = emailState.textChangeAndErrorOff(newEmail))

    fun onNameChange(newName: String) =
        copy(nameState = nameState.textChangeAndErrorOff(newName))

    fun onPasswordChange(newPassword: String) =
        copy(passwordState = passwordState.textChangeAndErrorOff(newPassword))

    fun onPasswordConfirmChange(newPasswordConfirm: String) =
        copy(passwordConfirmState = passwordConfirmState.textChangeAndErrorOff(newPasswordConfirm))

    fun setEmailError(error: TextFieldError) = copy(emailState = emailState.errorOn(error))

    fun setPasswordError(error: TextFieldError) = copy(passwordState = passwordState.errorOn(error))

    fun setPasswordConFirmError(error: TextFieldError) =
        copy(passwordConfirmState = passwordConfirmState.errorOn(error))

    fun passwordErrorOff() = copy(passwordState = passwordState.errorOff())

    fun passwordConfirmErrorOff() = copy(passwordConfirmState = passwordConfirmState.errorOff())

    fun setGoogleButtonEnabled(enabled: Boolean) = copy(googleButtonEnabled = enabled)

    fun passwordVisualTransformationToggle(isVisible: Boolean) =
        if (isVisible) copy(passwordState = passwordState.setInvisible())
        else copy(passwordState = passwordState.setVisible())

    fun passwordConfirmVisualTransformationToggle(isVisible: Boolean) =
        if (isVisible) copy(passwordConfirmState = passwordConfirmState.setInvisible())
        else copy(passwordConfirmState = passwordConfirmState.setVisible())

    fun loadingOn() = copy(isLoading = true)

    fun loadingOff() = copy(isLoading = false)

    fun showToast(@StringRes toastText: Int) = copy(toastText = toastText)

    fun toastShown() = copy(toastText = null)
}
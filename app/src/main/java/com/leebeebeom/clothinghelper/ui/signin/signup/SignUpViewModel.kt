package com.leebeebeom.clothinghelper.ui.signin.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.base.TextFieldError
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.signin.FirebaseErrorCode
import com.leebeebeom.clothinghelper.ui.signin.GoogleSignInImpl

class SignUpViewModel : ViewModel(), GoogleSignInImpl {
    var signUpState by mutableStateOf(SignUpUIState())
        private set

    val onEmailChange = { newEmail: String ->
        emailUpdate(signUpState.emailState.textChangeAndErrorOff(newEmail))
    }

    val onNameChange = { newName: String ->
        nameUpdate(signUpState.nameState.textChangeAndErrorOff(newName))
    }

    val onPasswordChange = { newPassword: String ->
        passwordUpdate(signUpState.passwordState.textChangeAndErrorOff(newPassword))

        passwordSameCheck()
        if (newPassword.length < 6) passwordUpdate(signUpState.passwordState.errorOn(TextFieldError.ERROR_WEAK_PASSWORD))
        if (newPassword.isBlank()) passwordUpdate(signUpState.passwordState.errorOff())
    }

    val onPasswordConfirmChange = { newPasswordConfirm: String ->
        passwordConfirmUpdate(
            signUpState.passwordConfirmState.textChangeAndErrorOff(newPasswordConfirm)
        )

        passwordSameCheck()
        if (newPasswordConfirm.isBlank()) passwordConfirmUpdate(signUpState.passwordConfirmState.errorOff())
    }

    private fun passwordSameCheck() {
        val passwordConfirmState = signUpState.passwordConfirmState
        val passwordSate = signUpState.passwordState

        if (!passwordConfirmState.isBlank)
            if (passwordSate.text != passwordConfirmState.text)
                passwordConfirmUpdate(signUpState.passwordConfirmState.errorOn(TextFieldError.ERROR_PASSWORD_CONFIRM_NOT_SAME))
            else passwordConfirmUpdate(signUpState.passwordConfirmState.errorOff())
    }

    val passwordVisualTransformationToggle = { isVisible: Boolean ->
        passwordUpdate(
            if (isVisible) signUpState.passwordState.setInvisible()
            else signUpState.passwordState.setVisible()
        )
    }
    val passwordConfirmVisualTransformationToggle = { isVisible: Boolean ->
        passwordConfirmUpdate(
            if (isVisible) signUpState.passwordConfirmState.setInvisible()
            else signUpState.passwordConfirmState.setVisible()
        )
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
                } else setFirebaseError(it.exception)
                loadingOff()
            }
    }

    private fun userNameUpdate(user: FirebaseUser) {
        val request = userProfileChangeRequest { displayName = signUpState.nameState.text }
        user.updateProfile(request).addOnCompleteListener {
            if (it.isSuccessful) /* TODO 메인 액티비티 닉네임 업데이트 */
            else showToast(R.string.name_update_failed)
        }
    }

    private fun setFirebaseError(exception: Exception?) {
        when ((exception as FirebaseAuthException).errorCode) {
            FirebaseErrorCode.ERROR_INVALID_EMAIL -> setEmailError(TextFieldError.ERROR_INVALID_EMAIL)
            FirebaseErrorCode.ERROR_EMAIL_ALREADY_IN_USE -> setEmailError(TextFieldError.ERROR_EMAIL_ALREADY_IN_USE)
            else -> showToast(R.string.unknown_error)
        }
    }

    private fun setEmailError(error: TextFieldError) =
        emailUpdate(signUpState.emailState.errorOn(error))

    override fun loadingOn() = update(signUpState.copy(isLoading = true))

    override fun loadingOff() = update(signUpState.copy(isLoading = false))

    override fun showToast(resId: Int) = update(signUpState.copy(toastTextId = resId))

    val toastShown = update(signUpState.copy(toastTextId = null))

    private fun passwordConfirmUpdate(newPasswordConfirmState: TextFieldUIState) =
        update(signUpState.copy(passwordConfirmState = newPasswordConfirmState))

    private fun passwordUpdate(newPasswordState: TextFieldUIState) =
        update(signUpState.copy(passwordState = newPasswordState))

    private fun nameUpdate(newNameState: TextFieldUIState) =
        update(signUpState.copy(nameState = newNameState))

    private fun emailUpdate(newEmailState: TextFieldUIState) =
        update(signUpState.copy(emailState = newEmailState))

    private fun update(newUIState: SignUpUIState) {
        signUpState = newUIState
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
    val toastTextId: Int? = null
) {
    val signUpButtonEnable
        get() = !emailState.isBlank && !emailState.isError &&
                !nameState.isBlank && !nameState.isError &&
                !passwordState.isBlank && !passwordState.isError &&
                !passwordConfirmState.isBlank && !passwordConfirmState.isError
}
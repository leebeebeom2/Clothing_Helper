package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.FireBaseListeners
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepositoryImpl : UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private val isSignIn = MutableStateFlow(auth.currentUser != null)
    override suspend fun isSignIn(): StateFlow<Boolean> {
        auth.addAuthStateListener {
            isSignIn.value = it.currentUser != null
        }
        return isSignIn
    }

    private val user = MutableStateFlow(User())
    override suspend fun getUser(): StateFlow<User> {
        auth.addAuthStateListener {
            user.value =
                if (it.currentUser != null) {
                    val user = it.currentUser!!
                    User(
                        email = user.email!!,
                        name = user.displayName!!,
                        uid = user.uid
                    )
                } else User()

        }
        return user
    }

    override fun googleSignIn(
        googleCredential: Any?,
        googleSignInListener: FireBaseListeners.GoogleSignInListener
    ): Boolean {
        googleSignInListener.taskStart()

        var isFirstUser = false

        if (googleCredential is AuthCredential)
            FirebaseAuth.getInstance().signInWithCredential(googleCredential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        googleSignInListener.taskSuccess()
                        it.result.additionalUserInfo?.isNewUser?.run { isFirstUser = this }
                    } else googleSignInListener.taskFailed(it.exception)
                    googleSignInListener.taskFinish()
                }
        else googleSignInListener.googleCredentialCastFailed()

        return isFirstUser
    }

    override fun signIn(
        email: String,
        password: String,
        signInListener: FireBaseListeners.SignInListener
    ) {
        signInListener.taskStart()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) signInListener.taskSuccess()
            else signInListener.taskFailed(it.exception)
            signInListener.taskFinish()
        }
    }

    override fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FireBaseListeners.SignUpListener,
        updateNameListener: FireBaseListeners.UpdateNameListener
    ): Boolean {
        signUpListener.taskStart()

        var isFirstUser = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signUpListener.taskSuccess()
                it.result.additionalUserInfo?.isNewUser?.run { isFirstUser = this }
                //이름 업데이트
                val user = it.result.user
                if (user == null) updateNameListener.userNull()
                else updateName(updateNameListener, user, name)

            } else signUpListener.taskFailed(it.exception)
            signUpListener.taskFinish()
        }
        return isFirstUser
    }

    private fun updateName(
        updateNameListener: FireBaseListeners.UpdateNameListener, user: FirebaseUser, name: String
    ) {
        updateNameListener.taskStart()

        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request).addOnCompleteListener {
            if (it.isSuccessful) {
                updateName(name)
                updateNameListener.taskSuccess()
            } else updateNameListener.nameUpdateFailed()
            updateNameListener.taskFinish()
        }
    }

    private fun updateName(name: String) {
        user.value = user.value.copy(name = name)
    }

    override fun resetPasswordEmail(
        email: String, resetPasswordListener: FireBaseListeners.ResetPasswordListener
    ) {
        resetPasswordListener.taskStart()

        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) resetPasswordListener.taskSuccess()
            else resetPasswordListener.taskFailed(it.exception)
            resetPasswordListener.taskFinish()
        }
    }
}
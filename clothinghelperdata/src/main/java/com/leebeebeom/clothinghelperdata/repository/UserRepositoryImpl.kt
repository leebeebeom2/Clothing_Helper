package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.FireBaseListeners
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepositoryImpl : UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private val isSignIn = MutableStateFlow(auth.currentUser != null)
    override fun isSignIn(): StateFlow<Boolean> {
        isSignIn.value = auth.currentUser != null
        auth.addAuthStateListener { isSignIn.value = it.currentUser != null }
        return isSignIn
    }

    private val user = MutableStateFlow(User())
    override fun getUser(): StateFlow<User> {
        user.value = auth.currentUser.toUser()
        auth.addAuthStateListener { user.value = it.currentUser.toUser() }
        return user
    }

    override suspend fun googleSignIn(
        googleCredential: Any?, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ): Boolean = coroutineScope {
        googleSignInListener.taskStart()

        var isFirstUser = false

        if (googleCredential is AuthCredential) FirebaseAuth.getInstance()
            .signInWithCredential(googleCredential).addOnCompleteListener {
                if (it.isSuccessful) {
                    googleSignInListener.taskSuccess()
                    it.result.additionalUserInfo?.isNewUser?.run { isFirstUser = this }
                } else googleSignInListener.taskFailed(it.exception)
                googleSignInListener.taskFinish()
            }
        else googleSignInListener.googleCredentialCastFailed()
        isFirstUser
    }


    override suspend fun signIn(
        email: String, password: String, signInListener: FireBaseListeners.SignInListener
    ): Unit = coroutineScope {
        signInListener.taskStart()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) signInListener.taskSuccess()
            else signInListener.taskFailed(it.exception)
            signInListener.taskFinish()
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FireBaseListeners.SignUpListener,
        updateNameListener: FireBaseListeners.UpdateNameListener
    ): Unit = coroutineScope {
        signUpListener.taskStart()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signUpListener.taskSuccess()
                //이름 업데이트
                val user = it.result.user
                if (user == null) updateNameListener.userNull()
                else updateName(updateNameListener, user, name)
            } else signUpListener.taskFailed(it.exception)
            signUpListener.taskFinish()
        }
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

    override suspend fun resetPasswordEmail(
        email: String, resetPasswordListener: FireBaseListeners.ResetPasswordListener
    ): Unit = coroutineScope {
        resetPasswordListener.taskStart()

        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) resetPasswordListener.taskSuccess()
            else resetPasswordListener.taskFailed(it.exception)
            resetPasswordListener.taskFinish()
        }
    }
}

fun FirebaseUser?.toUser() = if (this == null) User()
else User(email ?: "", displayName ?: "", uid)
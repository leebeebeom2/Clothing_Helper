package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.leebeebeom.clothinghelperdata.datasource.UserRemoteDataSource
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.FireBaseListeners
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow

class UserRepositoryImpl(userRemoteDataSource: UserRemoteDataSource) : UserRepository {
    private val auth = userRemoteDataSource.auth

    override val isLogin = MutableStateFlow(auth.currentUser != null)
    override var isFirstUser = false

    init {
        auth.addAuthStateListener { isLogin.value = it.currentUser != null }
    }

    override fun googleSignIn(
        googleCredential: Any?, googleSignInListener: FireBaseListeners.GoogleSignInListener
    ) {
        googleSignInListener.taskStart()

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
    }

    override fun getUser(): User =
        if (isLogin.value) {
            val user = auth.currentUser!!
            User(
                isLogin = true,
                email = user.email!!,
                name = user.displayName!!,
                uid = user.uid
            )
        } else User()

    override fun signIn(
        email: String, password: String, signInListener: FireBaseListeners.SignInListener
    ) {
        signInListener.taskStart()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signInListener.taskSuccess()
                it.result.additionalUserInfo?.isNewUser?.run { isFirstUser = this }
            } else signInListener.taskFailed(it.exception)
            signInListener.taskFinish()
        }
    }

    override fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FireBaseListeners.SignUpListener,
        updateNameListener: FireBaseListeners.UpdateNameListener
    ) {
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
        //TODO
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
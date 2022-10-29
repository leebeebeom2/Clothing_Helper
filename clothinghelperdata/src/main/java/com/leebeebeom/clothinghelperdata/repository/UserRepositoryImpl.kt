package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelperdomain.model.SignIn
import com.leebeebeom.clothinghelperdomain.model.SignUp
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener2
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepositoryImpl : UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private val _isSignIn = MutableStateFlow(auth.currentUser != null)
    override val isSignIn: StateFlow<Boolean> get() = _isSignIn

    private val _user = MutableStateFlow(auth.currentUser.toUser())
    override val user: StateFlow<User?> get() = _user

    override fun googleSignIn(
        credential: Any?,
        listener: FirebaseListener2,
        pushInitialSubCategories: (uid: String) -> Unit
    ) {
        val authCredential = credential as? AuthCredential
        if (authCredential == null) {
            listener.taskFailed(NullPointerException("googleCredential is null"))
            return
        }

        auth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful) {
                val userObj = it.result.user.toUser()!!
                if (it.result.additionalUserInfo!!.isNewUser)
                    pushFirstUser(userObj, pushInitialSubCategories)
                signInSuccess(userObj)
                listener.taskSuccess()
            } else listener.taskFailed(it.exception)
            listener.taskFinish()
        }
    }

    override fun signIn(
        signIn: SignIn,
        listener: FirebaseListener2,
    ) {
        auth.signInWithEmailAndPassword(signIn.email, signIn.password).addOnCompleteListener {
            if (it.isSuccessful) {
                val userObj = it.result.user.toUser()!!
                signInSuccess(userObj)
                listener.taskSuccess()
            } else listener.taskFailed(it.exception)
            listener.taskFinish()
        }
    }

    override fun signUp(
        signUp: SignUp,
        signUpListener: FirebaseListener2,
        updateNameListener: FirebaseListener2,
        pushInitialSubCategories: (uid: String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(signUp.email, signUp.password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user!!
                val userObj = user.toUser()!!
                pushFirstUser(userObj, pushInitialSubCategories)
                signInSuccess(userObj)
                signUpListener.taskSuccess()
                updateName(updateNameListener, user, signUp.name)
            } else signUpListener.taskFailed(it.exception)
            signUpListener.taskFinish()
        }
    }

    private fun updateName(updateNameListener: FirebaseListener2, user: FirebaseUser, name: String) {
        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val newNameUser = user.toUser()?.copy(name = name)!!
                    pushUser(newNameUser)
                    updateUser(newNameUser)
                    updateNameListener.taskSuccess()
                } else updateNameListener.taskFailed(it.exception)
                updateNameListener.taskFinish()
            }
    }

    override fun resetPasswordEmail(
        email: String,
        resetPasswordListener: FirebaseListener,
        taskFinish: () -> Unit
    ) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) resetPasswordListener.taskSuccess()
            else resetPasswordListener.taskFailed(it.exception)
            taskFinish()
        }
    }

    private fun pushFirstUser(user: User, writeInitialSubCategory: (String) -> Unit) {
        pushUser(user)
        writeInitialSubCategory(user.uid)
    }

    private fun pushUser(user: User) =
        FirebaseDatabase.getInstance().reference.child(user.uid)
            .child(DatabasePath.USER_INFO).setValue(user)

    override fun signOut() {
        auth.signOut()
        _isSignIn.value = false
        this._user.value = null
    }

    private fun signInSuccess(user: User) {
        _isSignIn.value = true
        updateUser(user)
    }

    private fun updateUser(user: User) {
        this._user.value = user
    }
}

fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName ?: "", uid) }
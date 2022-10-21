package com.leebeebeom.clothinghelperdata.repository

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepositoryImpl(val subCategoryRepositoryImpl: SubCategoryRepositoryImpl) :
    UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private lateinit var _isSignIn: MutableStateFlow<Boolean>
    override fun isSignIn(): StateFlow<Boolean> {
        if (!::_isSignIn.isInitialized) _isSignIn = MutableStateFlow(auth.currentUser != null)
        return _isSignIn
    }

    private lateinit var user: MutableStateFlow<User?>
    override fun getUser(): StateFlow<User?> {
        if (!::user.isInitialized) user = MutableStateFlow(auth.currentUser.toUser())
        return user
    }

    private fun signInSuccess(user: User) {
        _isSignIn.value = true
        updateUser(user)
    }

    private fun updateUser(user: User) {
        if (!::user.isInitialized) this.user = MutableStateFlow(user)
        else this.user.value = user
    }

    private fun firstUserTask(user: User) {
        pushUser(user)
        subCategoryRepositoryImpl.writeInitialSubCategory(user.uid)
    }

    override fun googleSignIn(googleCredential: Any?, googleSignInListener: FirebaseListener) {
        val authCredential = googleCredential as AuthCredential

        auth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user.toUser()!!
                it.result.additionalUserInfo?.isNewUser?.let {
                    firstUserTask(user)
                }
                signInSuccess(user)
                googleSignInListener.taskSuccess()
                Log.d("TAG", "googleSignIn: 태스크 끝")
            } else googleSignInListener.taskFailed(it.exception)
        }
    }

    override fun signIn(email: String, password: String, signInListener: FirebaseListener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) signInListener.taskSuccess()
            else signInListener.taskFailed(it.exception)
        }
    }

    override fun signUp(
        email: String,
        password: String,
        name: String,
        signUpListener: FirebaseListener,
        updateNameListener: FirebaseListener
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user!!
                firstUserTask(user.toUser()!!)
                signUpListener.taskSuccess()
                updateName(updateNameListener, user, name)
            } else signUpListener.taskFailed(it.exception)
        }
    }

    private fun updateName(updateNameListener: FirebaseListener, user: FirebaseUser, name: String) {
        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request).addOnCompleteListener {
            if (it.isSuccessful) {
                val newNameUser = user.toUser()!!.copy(name = name)
                pushUser(newNameUser)
                updateUser(newNameUser)
                updateNameListener.taskSuccess()
            } else updateNameListener.taskFailed(null)
        }
    }

    override fun resetPasswordEmail(email: String, resetPasswordListener: FirebaseListener) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) resetPasswordListener.taskSuccess()
            else resetPasswordListener.taskFailed(it.exception)
        }
    }

    private fun pushUser(user: User) =
        FirebaseDatabase.getInstance().reference.child(user.uid).child(DatabasePath.USER_INFO)
            .setValue(user).addOnCompleteListener {
                if (!it.isSuccessful) throw Exception("pushUser 실패")
            }
}

fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName ?: "", uid) }
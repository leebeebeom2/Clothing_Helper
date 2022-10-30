package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.SignIn
import com.leebeebeom.clothinghelperdomain.model.SignUp
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.repository.onDone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRepositoryImpl : UserRepository {
    private val auth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isSignIn = MutableStateFlow(auth.currentUser != null)
    override val isSignIn: StateFlow<Boolean> get() = _isSignIn

    private val _user = MutableStateFlow(auth.currentUser.toUser())
    override val user: StateFlow<User?> get() = _user

    override fun googleSignIn(
        credential: Any?,
        onDone: onDone,
        pushInitialSubCategories: (uid: String) -> Unit
    ) {
        loadingOn()

        val authCredential = credential as AuthCredential

        auth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful) {
                val userObj = it.result.user.toUser()!!
                if (it.result.additionalUserInfo!!.isNewUser)
                    pushFirstUserData(userObj, pushInitialSubCategories)
                updateSignIn(true)
                updateUser(userObj)
                onDone(FirebaseResult.Success)
            } else onDone(FirebaseResult.Fail(it.exception))

            loadingOff()
        }
    }

    override fun signIn(signIn: SignIn, onDone: onDone) {
        loadingOn()

        auth.signInWithEmailAndPassword(signIn.email, signIn.password).addOnCompleteListener {
            if (it.isSuccessful) {
                val userObj = it.result.user.toUser()!!
                updateSignIn(true)
                updateUser(userObj)
                onDone(FirebaseResult.Success)
            } else onDone(FirebaseResult.Fail(it.exception))

            loadingOff()
        }
    }

    override fun signUp(
        signUp: SignUp,
        onSignUpDone: onDone,
        onNameUpdateDone: onDone,
        pushInitialSubCategories: (uid: String) -> Unit
    ) {
        loadingOn()

        auth.createUserWithEmailAndPassword(signUp.email, signUp.password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user!!
                val userObj = user.toUser()!!
                pushFirstUserData(userObj, pushInitialSubCategories)
                updateSignIn(true)
                updateUser(userObj)
                onSignUpDone(FirebaseResult.Success)
                updateName(onNameUpdateDone, user, signUp.name)
            } else onSignUpDone(FirebaseResult.Fail(it.exception))

            loadingOff()
        }
    }

    private fun updateName(
        onNameUpdateDone: onDone,
        user: FirebaseUser,
        name: String
    ) {
        loadingOn()

        val request = userProfileChangeRequest { displayName = name }

        user.updateProfile(request)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val newNameUser = user.toUser()?.copy(name = name)!!
                    pushUser(newNameUser)
                    updateUser(newNameUser)
                    onNameUpdateDone(FirebaseResult.Success)
                } else onNameUpdateDone(FirebaseResult.Fail(it.exception))

                loadingOff()
            }
    }

    override fun resetPasswordEmail(email: String, onDone: onDone) {
        loadingOn()

        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) onDone(FirebaseResult.Success)
            else onDone(FirebaseResult.Fail(it.exception))

            loadingOff()
        }
    }

    override fun signOut() {
        auth.signOut()
        updateSignIn(false)
        updateUser(null)
    }

    private fun pushFirstUserData(user: User, writeInitialSubCategory: (String) -> Unit) {
        pushUser(user)
        writeInitialSubCategory(user.uid)
    }

    private fun pushUser(user: User) =
        FirebaseDatabase.getInstance().reference.child(user.uid)
            .child(DatabasePath.USER_INFO).setValue(user)

    private fun updateSignIn(state: Boolean) {
        _isSignIn.value = state
    }

    private fun updateUser(user: User?) {
        this._user.value = user
    }

    private fun loadingOn() {
        _isLoading.value = true
    }

    private fun loadingOff() {
        _isLoading.value = false
    }
}

fun FirebaseUser?.toUser() = this?.let { User(email!!, displayName ?: "", uid) }
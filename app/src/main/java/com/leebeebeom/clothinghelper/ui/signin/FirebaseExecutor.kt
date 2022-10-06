package com.leebeebeom.clothinghelper.ui.signin

import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class FirebaseExecutor(private val onCompleteListener: ((Task<*>) -> Unit)? = null) {
    companion object {
        var user = mutableStateOf(FirebaseAuth.getInstance().currentUser)
            private set

        fun reLoadUser() {
            user.value = FirebaseAuth.getInstance().currentUser
        }

        fun addAuthStateListener(listener: FirebaseAuth.AuthStateListener) =
            FirebaseAuth.getInstance().addAuthStateListener(listener)

        fun removeAuthStateListener(listener: FirebaseAuth.AuthStateListener) =
            FirebaseAuth.getInstance().removeAuthStateListener(listener)


        fun signOut() = FirebaseAuth.getInstance().signOut()

        fun updateName(name: String, onCompleteListener: (Task<*>) -> Unit) {
            val request = userProfileChangeRequest { displayName = name }
            user.value?.updateProfile(request)?.addOnCompleteListener(onCompleteListener)
        }

        val userName get() = user.value?.displayName ?: "이름 없음"
        val userEmail get() = user.value?.email ?: "이메일 없음"
    }

    fun signInWithCredential(credential: AuthCredential) {
        addOnCompleteListener(
            getFirebaseAuthInstance()
                .signInWithCredential(credential)
        )
    }

    fun sendResetPasswordEmail(email: String) {
        addOnCompleteListener(
            getFirebaseAuthInstance()
                .sendPasswordResetEmail(email)
        )
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        addOnCompleteListener(
            getFirebaseAuthInstance()
                .signInWithEmailAndPassword(email, password)
        )
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        addOnCompleteListener(
            getFirebaseAuthInstance()
                .createUserWithEmailAndPassword(email, password)
        )
    }

    private fun addOnCompleteListener(task: Task<*>) =
        onCompleteListener?.let { task.addOnCompleteListener(it) }

    private fun getFirebaseAuthInstance() = FirebaseAuth.getInstance()
}
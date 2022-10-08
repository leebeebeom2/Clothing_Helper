package com.leebeebeom.clothinghelper.ui.signin

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class FirebaseUseCase(private val onCompleteListener: ((Task<*>) -> Unit)? = null) {
    companion object {
        private var user = FirebaseAuth.getInstance().currentUser

        fun signOut() = FirebaseAuth.getInstance().signOut()

        fun updateName(name: String, onCompleteListener: (Task<*>) -> Unit) {
            val request = userProfileChangeRequest { displayName = name }
            user?.updateProfile(request)?.addOnCompleteListener(onCompleteListener)
        }

        val userName get() = user?.displayName ?: "이름 없음"
        val userEmail get() = user?.email ?: "이메일 없음"
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
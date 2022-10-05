package com.leebeebeom.clothinghelper.ui.signin

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth

class FirebaseExecution(private val onCompleteListener: (Task<*>) -> Unit) {

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
        task.addOnCompleteListener(onCompleteListener)

    private fun getFirebaseAuthInstance() = FirebaseAuth.getInstance()
}
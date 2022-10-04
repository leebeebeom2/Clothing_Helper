package com.leebeebeom.clothinghelper.ui.signin

import androidx.compose.runtime.MutableState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CHFirebase(private val onCompleteListener: (Task<*>) -> Unit) {

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

class FirebaseTaskStateDelegate(private val firebaseTaskState: MutableState<Boolean>) :
    ReadWriteProperty<SignInBaseViewModel, Boolean> {
    override fun getValue(thisRef: SignInBaseViewModel, property: KProperty<*>): Boolean {
        return if (firebaseTaskState.value) {
            firebaseTaskState.value = false
            true
        } else false
    }

    override fun setValue(thisRef: SignInBaseViewModel, property: KProperty<*>, value: Boolean) {
        firebaseTaskState.value = value
    }

}
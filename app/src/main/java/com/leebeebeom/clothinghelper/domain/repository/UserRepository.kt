package com.leebeebeom.clothinghelper.domain.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import com.leebeebeom.clothinghelper.domain.model.User
import kotlinx.coroutines.flow.SharedFlow

interface UserRepository : LoadingStateProvider {
    val user: SharedFlow<User?>

    suspend fun googleSignIn(credential: AuthCredential)

    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, NotFoundUser, WrongPassword 등
     */
    suspend fun signIn(email: String, password: String)

    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, EmailAlreadyInUse 등
     */
    suspend fun signUp(email: String, password: String, name: String)

    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, NotFoundUser 등
     */
    suspend fun sendResetPasswordEmail(email: String)

    fun signOut()
}
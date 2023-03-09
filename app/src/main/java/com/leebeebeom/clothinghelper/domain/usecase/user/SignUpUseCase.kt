package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val userRepository: UserRepository) {
    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, EmailAlreadyInUse 등
     */
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
    ) = userRepository.signUp(
        email = email,
        password = password,
        name = name
    )
}
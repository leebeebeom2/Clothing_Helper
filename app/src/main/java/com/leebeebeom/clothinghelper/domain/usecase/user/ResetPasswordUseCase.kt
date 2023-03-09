package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val userRepository: UserRepository) {
    /**
     * @throws FirebaseNetworkException - 인터넷에 연결되지 않았을 경우
     * @throws FirebaseTooManyRequestsException - 너무 많은 요청이 발생했을 경우
     * @throws FirebaseAuthException - InvalidEmail, NotFoundUser 등
     */
    suspend fun sendResetPasswordEmail(email: String) =
        userRepository.sendResetPasswordEmail(email = email)
}
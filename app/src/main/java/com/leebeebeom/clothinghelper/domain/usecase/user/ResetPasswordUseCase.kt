package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun sendResetPasswordEmail(
        email: String, firebaseResult: FirebaseResult,
    ) = userRepository.sendResetPasswordEmail(
        email = email,
        firebaseResult = firebaseResult,
    )
}
package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun sendResetPasswordEmail(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        email: String, firebaseResult: FirebaseResult,
    ) = userRepository.sendResetPasswordEmail(
        email = email,
        firebaseResult = firebaseResult,
        dispatcher = dispatcher
    )
}
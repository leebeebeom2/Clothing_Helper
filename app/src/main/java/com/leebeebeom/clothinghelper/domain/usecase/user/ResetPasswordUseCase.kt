package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ResetPasswordUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun sendResetPasswordEmail(email: String, firebaseResult: FirebaseResult) =
        userRepository.resetPasswordEmail(email = email, firebaseResult = firebaseResult)
}
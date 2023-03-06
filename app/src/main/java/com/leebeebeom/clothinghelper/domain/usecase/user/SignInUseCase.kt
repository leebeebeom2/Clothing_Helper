package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun signIn(
        email: String, password: String, firebaseResult: FirebaseResult,
    ) = userRepository.signIn(
        email = email,
        password = password,
        firebaseResult = firebaseResult,
    )
}
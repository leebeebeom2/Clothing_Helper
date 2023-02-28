package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        firebaseResult: FirebaseResult,
    ) = userRepository.signUp(
        email = email,
        password = password,
        name = name,
        firebaseResult = firebaseResult,
    )
}
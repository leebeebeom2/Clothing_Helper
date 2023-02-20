package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun signIn(email: String, password: String) =
        userRepository.signIn(email = email, password = password)
}
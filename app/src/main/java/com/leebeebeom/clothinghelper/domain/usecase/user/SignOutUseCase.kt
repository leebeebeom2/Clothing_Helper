package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun signOut() = userRepository.signOut()
}
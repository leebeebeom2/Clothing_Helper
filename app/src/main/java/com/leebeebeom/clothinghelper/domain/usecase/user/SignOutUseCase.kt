package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SignOutUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun signOut(onFail: (Exception) -> Unit) = userRepository.signOut(onFail = onFail)
}
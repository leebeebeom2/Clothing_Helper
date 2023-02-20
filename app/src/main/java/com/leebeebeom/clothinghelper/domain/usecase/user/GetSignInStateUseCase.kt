package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetSignInStateUseCase @Inject constructor(private val userRepository: UserRepository) {
    val isSignIn get() = userRepository.isSignIn
}
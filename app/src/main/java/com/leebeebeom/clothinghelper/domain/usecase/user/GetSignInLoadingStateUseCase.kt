package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import javax.inject.Inject

class GetSignInLoadingStateUseCase @Inject constructor(private val userRepository: UserRepository) {
    val signInLoadingStream get() = userRepository.loadingStream
}
package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetSignInStateUseCase @Inject constructor(private val userRepository: UserRepository) {
    val isSignIn get() = userRepository.isSignIn
}
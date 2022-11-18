package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetSignInLoadingStateUseCase @Inject constructor(private val userRepository: UserRepository) {
    val isLoading get() = userRepository.isLoading
}
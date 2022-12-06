package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun signIn(email: String, password: String) = userRepository.signIn(
        email = email,
        password = password
    )
}
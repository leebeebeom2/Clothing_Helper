package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.auth.AuthCredential
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GoogleSignInUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun googleSignIn(credential: AuthCredential) =
        userRepository.googleSignIn(credential = credential)
}
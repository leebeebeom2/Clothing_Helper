package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.auth.AuthCredential
import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun googleSignIn(credential: AuthCredential, firebaseResult: FirebaseResult) =
        userRepository.googleSignIn(credential = credential, firebaseResult = firebaseResult)
}
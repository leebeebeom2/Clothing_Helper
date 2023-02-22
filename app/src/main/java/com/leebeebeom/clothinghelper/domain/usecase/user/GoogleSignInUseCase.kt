package com.leebeebeom.clothinghelper.domain.usecase.user

import com.google.firebase.auth.AuthCredential
import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.usecase.AllLocalDataClearUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GoogleSignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val allLocalDataClearUseCase: AllLocalDataClearUseCase,
) {
    suspend fun googleSignIn(credential: AuthCredential, firebaseResult: FirebaseResult) {
        allLocalDataClearUseCase.allLocalDataClear()
        userRepository.googleSignIn(credential = credential, firebaseResult = firebaseResult)
    }
}
package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.usecase.AllLocalDataClearUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val allLocalDataClearUseCase: AllLocalDataClearUseCase,
) {
    suspend fun signIn(email: String, password: String, firebaseResult: FirebaseResult) =
        userRepository.signIn(
            email = email, password = password, firebaseResult = firebaseResult,
            allLocalDataClear = allLocalDataClearUseCase::allLocalDataClear
        )
}
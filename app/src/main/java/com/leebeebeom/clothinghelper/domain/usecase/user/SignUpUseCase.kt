package com.leebeebeom.clothinghelper.domain.usecase.user

import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.usecase.AllLocalDataClearUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val allLocalDataClearUseCase: AllLocalDataClearUseCase
) {
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        firebaseResult: FirebaseResult,
    ) = userRepository.signUp(
        email = email,
        password = password,
        name = name,
        firebaseResult = firebaseResult,
        allLocalDataClear = allLocalDataClearUseCase::allLocalDataClear
    )
}
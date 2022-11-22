package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.PushInitialSubCategoriesUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase
) {
    /**
     * 가입 성공 시 초기 서브 카테고리 푸쉬
     */
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        onUpdateSubCategoriesFail: (FirebaseResult) -> Unit
    ): AuthResult {
        val authResult = userRepository.signUp(email = email, password = password, name = name)

        if (authResult is AuthResult.Success)
            pushInitialSubCategoriesUseCase.pushInitialSubCategories(
                authResult.user.uid,
                onUpdateSubCategoriesFail
            )
        return authResult
    }
}
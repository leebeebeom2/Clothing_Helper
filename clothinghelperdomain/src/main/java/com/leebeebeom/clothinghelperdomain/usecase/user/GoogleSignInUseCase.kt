package com.leebeebeom.clothinghelperdomain.usecase.user

import com.leebeebeom.clothinghelperdomain.model.AuthResult
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.PushInitialSubCategoriesUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GoogleSignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val pushInitialSubCategoriesUseCase: PushInitialSubCategoriesUseCase
) {
    /**
     * 첫 유저일 시 초기 서브 카테고리 푸쉬
     */
    suspend fun googleSignIn(
        credential: Any?,
        onSubCategoriesLoadFail: (FirebaseResult.Fail) -> Unit
    ): AuthResult {
        val authResult = userRepository.googleSignIn(credential)

        if (authResult is AuthResult.Success && authResult.isNewer)
            pushInitialSubCategoriesUseCase.pushInitialSubCategories(
                authResult.user.uid,
                onSubCategoriesLoadFail
            )
        return authResult
    }
}
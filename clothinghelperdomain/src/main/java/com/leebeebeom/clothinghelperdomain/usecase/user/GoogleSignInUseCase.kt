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
    suspend fun googleSignIn(
        credential: Any?,
        onSubCategoriesLoadFail: (FirebaseResult.Fail) -> Unit
    ): AuthResult {
        val authResult = userRepository.googleSignIn(credential = credential)

        if (authResult is AuthResult.Success && authResult.isNewer)
            pushInitialSubCategoriesUseCase.pushInitialSubCategories(
                uid = authResult.user.uid,
                onLoadSubCategoriesFail = onSubCategoriesLoadFail
            )
        return authResult
    }
}
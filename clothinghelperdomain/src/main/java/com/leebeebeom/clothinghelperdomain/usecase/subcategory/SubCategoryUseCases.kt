package com.leebeebeom.clothinghelperdomain.usecase.subcategory

import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WriteInitialSubCategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(user: User) =
        withContext(Dispatchers.IO) { subCategoryRepository.writeInitialSubCategory(user) }
}

class GetSubCategoriesUserCase(
    private val getTopSubcategoriesUseCase: GetTopSubcategoriesUseCase,
    private val getBottomSubcategoriesUseCase: GetBottomSubcategoriesUseCase,
    private val getOuterSubcategoriesUseCase: GetOuterSubcategoriesUseCase,
    private val getEtcSubcategoriesUseCase: GetEtcSubcategoriesUseCase
) {
    suspend fun getTopSubCategories(uid: String, onCancelled: (Int, String) -> Unit) =
        withContext(Dispatchers.IO) {
            getTopSubcategoriesUseCase.invoke(uid, onCancelled)
        }

    suspend fun getBottomSubCategories(uid: String, onCancelled: (Int, String) -> Unit) =
        withContext(Dispatchers.IO) {
            getBottomSubcategoriesUseCase.invoke(uid, onCancelled)
        }

    suspend fun getOuterSubCategories(uid: String, onCancelled: (Int, String) -> Unit) =
        withContext(Dispatchers.IO) {
            getOuterSubcategoriesUseCase.invoke(uid, onCancelled)

        }

    suspend fun getEtcSubCategories(uid: String, onCancelled: (Int, String) -> Unit) =
        withContext(Dispatchers.IO) {
            getEtcSubcategoriesUseCase.invoke(uid, onCancelled)
        }
}

class GetTopSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(uid: String, onCancelled: (Int, String) -> Unit) =
        withContext(Dispatchers.IO)
        { subCategoryRepository.getTopSubCategories(uid, onCancelled) }
}

class GetBottomSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(uid: String, onCancelled: (Int, String) -> Unit) =
        withContext(Dispatchers.IO)
        { subCategoryRepository.getBottomSubCategories(uid, onCancelled) }
}

class GetOuterSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(uid: String, onCancelled: (Int, String) -> Unit) =
        withContext(Dispatchers.IO)
        { subCategoryRepository.getOuterSubCategories(uid, onCancelled) }
}

class GetEtcSubcategoriesUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend operator fun invoke(uid: String, onCancelled: (Int, String) -> Unit) =
        withContext(Dispatchers.IO)
        { subCategoryRepository.getEtcSubCategories(uid, onCancelled) }
}


class AddSubCategoryUseCase(
    private val subCategoryRepository: SubCategoryRepository
) {
    suspend operator fun invoke(
        uid: String,
        subCategoryParent: SubCategoryParent,
        name: String,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) = withContext(Dispatchers.IO) {
        subCategoryRepository.addSubCategory(
            uid = uid,
            subCategoryParent = subCategoryParent,
            name = name,
            onSuccess = onSuccess,
            onFailed = onFailed
        )
    }
}
package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.addUseCaseTest
import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddSubCategoryUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var subCategoryRepository: SubCategoryRepository
    private lateinit var addSubCategoryUseCase: AddSubCategoryUseCase
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)

    @Before
    fun init() {
        userRepository = repositoryProvider.getUserRepository()
        subCategoryRepository = repositoryProvider.getSubCategoryRepository()
        addSubCategoryUseCase = AddSubCategoryUseCase(subCategoryRepository)
    }

    @Test
    fun subCategoryAddTest() = runTest(dispatcher) {
        suspend fun add(name: String) = addSubCategoryUseCase.add(dispatcher = dispatcher,
            name = name,
            mainCategoryType = MainCategoryType.TOP,
            uid = userRepository.user.value!!.uid,
            onFail = { assert(false) })

        addUseCaseTest(
            dispatcher = dispatcher,
            userRepository = userRepository,
            repository = subCategoryRepository,
            refPath = DatabasePath.SUB_CATEGORIES,
            add = ::add
        )
    }
}
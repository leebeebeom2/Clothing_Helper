package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.addContainerUseCaseTest
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddSubCategoryUseCaseTest {
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<SubCategory, SubCategoryRepository>
    private lateinit var addSubCategoryUseCase: AddSubCategoryUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        dataRepositoryTestUtil =
            DataRepositoryTestUtil(
                repositoryProvider = repositoryProvider,
                repository = repositoryProvider.createSubCategoryRepository()
            )
        addSubCategoryUseCase =
            AddSubCategoryUseCase(subCategoryRepository = dataRepositoryTestUtil.repository)
    }

    @Test
    fun subCategoryAddTest() = runTest(dispatcher) {
        addContainerUseCaseTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            add = {
                addSubCategoryUseCase.add(
                    name = it,
                    mainCategoryType = MainCategoryType.TOP,
                    uid = dataRepositoryTestUtil.userRepositoryTestUtil.uid!!,
                    onFail = { assert(false) })
            }
        )
    }
}
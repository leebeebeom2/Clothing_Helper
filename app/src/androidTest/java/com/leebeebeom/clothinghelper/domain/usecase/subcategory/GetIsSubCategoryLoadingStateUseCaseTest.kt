package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.dataLoadingFlowTest
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetIsSubCategoryLoadingStateUseCaseTest {
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<SubCategory, SubCategoryRepository>
    private lateinit var getIsSubCategoryLoadingStateUseCase: GetIsSubCategoryLoadingStateUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        dataRepositoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            repository = repositoryProvider.createSubCategoryRepository()
        )
        getIsSubCategoryLoadingStateUseCase = GetIsSubCategoryLoadingStateUseCase(
            dataRepositoryTestUtil.repository
        )
    }

    @Test
    fun subCategoryLoadingFlowTest() = runTest(dispatcher) {
        dataLoadingFlowTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            initDataList = iniDataList,
            loadingFlow = getIsSubCategoryLoadingStateUseCase.isLoading
        )
    }

    private val iniDataList = List(10) { SubCategory(name = "test $it") }
}
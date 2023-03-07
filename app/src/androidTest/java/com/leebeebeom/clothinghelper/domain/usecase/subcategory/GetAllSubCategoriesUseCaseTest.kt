package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.getAllDataUseCaseTest
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllSubCategoriesUseCaseTest {
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<SubCategory, SubCategoryRepository>
    private lateinit var getAllSubCategoriesUseCase: GetAllSubCategoriesUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        dataRepositoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            repository = repositoryProvider.createSubCategoryRepository()
        )
        getAllSubCategoriesUseCase = GetAllSubCategoriesUseCase(dataRepositoryTestUtil.repository)
    }

    @Test
    fun getAllDataTest() = runTest(dispatcher) {
        getAllDataUseCaseTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            initDataList = initDataLst,
            allDataFlow = getAllSubCategoriesUseCase.allSubCategories,
            assertLoadedData = { initDataList, loadedDataList ->
                assert(initDataList.map { it.name } == loadedDataList.map { it.name })
            }
        )
    }

    private val initDataLst = List(10) { SubCategory(name = "test $it") }
}
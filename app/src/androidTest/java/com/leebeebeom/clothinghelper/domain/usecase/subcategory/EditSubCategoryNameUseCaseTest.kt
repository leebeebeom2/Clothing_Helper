package com.leebeebeom.clothinghelper.domain.usecase.subcategory

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.editUseCaseTest
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditSubCategoryNameUseCaseTest {
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<SubCategory, SubCategoryRepository>
    private lateinit var addSubCategoryUseCase: AddSubCategoryUseCase
    private lateinit var editSubCategoryNameUseCase: EditSubCategoryNameUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        dataRepositoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider,
            repositoryProvider.createSubCategoryRepository()
        )
        addSubCategoryUseCase =
            AddSubCategoryUseCase(subCategoryRepository = dataRepositoryTestUtil.repository)
        editSubCategoryNameUseCase =
            EditSubCategoryNameUseCase(subCategoryRepository = dataRepositoryTestUtil.repository)

    }

    @Test
    fun nameEditTest() = runTest(dispatcher) {
        val subCategory = SubCategory(name = "test")

        editUseCaseTest(
            dataRepositoryTestUtil,
            addData = subCategory,
            edit = { oldData, name ->
                editSubCategoryNameUseCase.nameEdit(
                    oldSubCategory = oldData,
                    name = name,
                    uid = dataRepositoryTestUtil.userRepositoryTestUtil.uid!!,
                    onFail = { assert(false) }
                )
            }
        )
    }
}
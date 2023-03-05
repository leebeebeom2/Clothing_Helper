package com.leebeebeom.clothinghelper.data.container

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SubCategoryRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var subCategoryRepository: SubCategoryRepository
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher = dispatcher)

    @Before
    fun init() {
        userRepository = repositoryProvider.getUserRepository()
        subCategoryRepository = repositoryProvider.getSubCategoryRepository()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun crudTest() = runTest(dispatcher) {
        val subCategory = SubCategory(name = "subcategory test")

        repositoryCrudTest(
            dispatcher = dispatcher,
            userRepository = userRepository,
            data = subCategory,
            repository = subCategoryRepository,
            addAssert = {
                assert(it.name == subCategory.name)
                assert(it.createDate == it.editDate)
            },
            newData = { it.copy(name = "new subCategory") }
        ) { origin, new ->
            assert(origin.key == new.key)
            assert(origin.name != new.name)
            assert(new.name == "new subCategory")
            assert(origin.key == new.key)
            assert(origin.mainCategoryType == new.mainCategoryType)
            assert(origin.createDate == new.createDate)
            assert(origin.editDate != new.editDate)
            assert(origin.editDate < new.editDate)
        }
    }
}
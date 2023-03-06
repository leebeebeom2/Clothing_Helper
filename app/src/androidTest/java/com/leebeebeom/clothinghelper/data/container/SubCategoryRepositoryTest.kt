package com.leebeebeom.clothinghelper.data.container

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.data.sortTest
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SubCategoryRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var subCategoryPreferencesRepository: SortPreferenceRepository
    private lateinit var subCategoryRepository: SubCategoryRepository
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher = dispatcher)

    @Before
    fun init() {
        userRepository = repositoryProvider.getUserRepository()
        subCategoryPreferencesRepository = repositoryProvider.getSubCategoryPreferenceRepository()
        subCategoryRepository =
            repositoryProvider.getSubCategoryRepository(subCategoryPreferencesRepository)
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

    @Test
    fun sortTest() = runTest(dispatcher) {
        sortTest(
            dispatcher = dispatcher,
            preferencesRepository = subCategoryPreferencesRepository,
            userRepository = userRepository,
            repository = subCategoryRepository,
            data = subCategories,
            refPath = DatabasePath.SUB_CATEGORIES
        )
    }

    private var createdData = System.currentTimeMillis()
    private val subCategories =
        List(9) { SubCategory(name = "$it", createDate = createdData++, editDate = createdData++) }
}
package com.leebeebeom.clothinghelper.data.container

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.data.sortTest
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SubCategoryRepositoryTest {
    private lateinit var subCategoryPreferencesRepository: SortPreferenceRepository
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<SubCategory, SubCategoryRepository>
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        subCategoryPreferencesRepository =
            repositoryProvider.createSubCategoryPreferenceRepository()

        dataRepositoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider = RepositoryProvider(dispatcher = dispatcher),
            repository = repositoryProvider.createSubCategoryRepository(
                subCategoryPreferencesRepository = subCategoryPreferencesRepository
            )
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun crudTest() = runTest(dispatcher) {
        val subCategory = SubCategory(name = "subcategory test")

        repositoryCrudTest(
            backgroundScope = backgroundScope,
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            data = subCategory,
            addAssert = {
                assert(it.name == subCategory.name)
                assert(it.createDate == it.editDate)
            },
            editData = { it.copy(name = "new subCategory") },
            editAssert = { origin, new ->
                assert(origin.key == new.key)
                assert(origin.name != new.name)
                assert(new.name == "new subCategory")
                assert(origin.key == new.key)
                assert(origin.mainCategoryType == new.mainCategoryType)
                assert(origin.createDate == new.createDate)
                assert(origin.editDate != new.editDate)
                assert(origin.editDate < new.editDate)
            },
            loadAssert = { origin, loaded -> assert(origin == loaded) }
        )
    }

    @Test
    fun sortTest() = runTest(dispatcher) {
        sortTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            preferencesRepository = subCategoryPreferencesRepository,
            data = subCategories
        )
    }

    private var createdData = System.currentTimeMillis()
    private val subCategories =
        List(9) { SubCategory(name = "$it", createDate = createdData++, editDate = createdData++) }
}
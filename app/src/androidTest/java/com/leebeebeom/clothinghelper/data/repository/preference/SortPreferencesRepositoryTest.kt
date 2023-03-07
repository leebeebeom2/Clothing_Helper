package com.leebeebeom.clothinghelper.data.repository.preference

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SortPreferencesRepositoryTest {
    private lateinit var folderPreferencesRepository: SortPreferenceRepository
    private lateinit var subCategoryPreferencesRepository: SortPreferenceRepository
    private val dispatcher = StandardTestDispatcher()


    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        folderPreferencesRepository = repositoryProvider.createFolderPreferenceRepository()
        subCategoryPreferencesRepository = repositoryProvider.createSubCategoryPreferenceRepository()
    }

    @Test
    fun sotFlowTest() = runTest(dispatcher) {
        preferenceSortFlowTest(dispatcher = dispatcher, repository = folderPreferencesRepository)
        preferenceSortFlowTest(
            dispatcher = dispatcher, repository = subCategoryPreferencesRepository
        )
    }
}
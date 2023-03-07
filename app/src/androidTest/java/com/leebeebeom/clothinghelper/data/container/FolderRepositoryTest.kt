package com.leebeebeom.clothinghelper.data.container

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.data.sortTest
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FolderRepositoryTest {
    private lateinit var folderPreferencesRepository: SortPreferenceRepository
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<Folder, FolderRepository>
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        folderPreferencesRepository = repositoryProvider.createFolderPreferenceRepository()
        dataRepositoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider, repositoryProvider.createFolderRepository(
                folderPreferencesRepository = folderPreferencesRepository
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun crudTest() = runTest(dispatcher) {
        val folder = Folder(name = "folder")

        repositoryCrudTest(
            backgroundScope = backgroundScope,
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            data = folder,
            addAssert = {
                assert(it.name == folder.name)
                assert(it.createDate == it.editDate)
            },
            editData = { it.copy(name = "new folder") },
            editAssert = { origin, new ->
                assert(origin.key == new.key)
                assert(origin.name != new.name)
                assert(new.name == "new folder")
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
            preferencesRepository = folderPreferencesRepository,
            data = folders
        )
    }

    private
    var createdData = System.currentTimeMillis()

    private
    val folders =
        List(9) {
            Folder(
                name = "$it",
                createDate = createdData++,
                editDate = createdData++
            )
        }
}
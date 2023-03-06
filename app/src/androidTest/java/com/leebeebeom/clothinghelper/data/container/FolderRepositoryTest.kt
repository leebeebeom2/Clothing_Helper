package com.leebeebeom.clothinghelper.data.container

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.data.sortTest
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FolderRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var folderPreferencesRepository: SortPreferenceRepository
    private lateinit var folderRepository: FolderRepository
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher = dispatcher)

    @Before
    fun init() {
        userRepository = repositoryProvider.getUserRepository()
        folderPreferencesRepository = repositoryProvider.getFolderPreferenceRepository()
        folderRepository = repositoryProvider.getFolderRepository(folderPreferencesRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun crudTest() = runTest(dispatcher) {
        val folder = Folder(name = "folder")

        repositoryCrudTest(dispatcher = dispatcher,
            userRepository = userRepository,
            data = folder,
            repository = folderRepository,
            addAssert = {
                assert(it.name == folder.name)
                assert(it.createDate == it.editDate)
            },
            newData = { it.copy(name = "new folder") }) { origin, new ->
            assert(origin.key == new.key)
            assert(origin.name != new.name)
            assert(new.name == "new folder")
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
            preferencesRepository = folderPreferencesRepository,
            userRepository = userRepository,
            repository = folderRepository,
            data = folders,
            refPath = DatabasePath.FOLDERS
        )
    }

    private var createdData = System.currentTimeMillis()
    private val folders =
        List(9) { Folder(name = "$it", createDate = createdData++, editDate = createdData++) }
}
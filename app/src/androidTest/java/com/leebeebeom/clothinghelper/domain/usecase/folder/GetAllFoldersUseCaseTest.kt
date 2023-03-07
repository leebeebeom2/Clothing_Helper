package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.getAllDataUseCaseTest
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllFoldersUseCaseTest {
    private lateinit var getAllFoldersUseCase: GetAllFoldersUseCase
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<Folder, FolderRepository>
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        dataRepositoryTestUtil =
            DataRepositoryTestUtil(repositoryProvider, repositoryProvider.createFolderRepository())
        getAllFoldersUseCase = GetAllFoldersUseCase(dataRepositoryTestUtil.repository)
    }

    @Test
    fun getAllDataTest() = runTest(dispatcher) {
        getAllDataUseCaseTest(
            dataRepositoryTestUtil,
            initDataList = initDataList,
            allDataFlow = getAllFoldersUseCase.allFolders,
            assertLoadedData = { initDataList, loadedDataList ->
                assert(initDataList.map { it.name } == loadedDataList.map { it.name })
            }
        )
    }

    private val initDataList = List(8) { Folder(name = "test $it") }
}
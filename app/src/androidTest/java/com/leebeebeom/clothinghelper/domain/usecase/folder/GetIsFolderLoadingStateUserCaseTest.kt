package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.dataLoadingFlowTest
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetIsFolderLoadingStateUserCaseTest {
    private lateinit var getIsFolderLoadingStateUserCase: GetIsFolderLoadingStateUserCase
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<Folder, FolderRepository>
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        dataRepositoryTestUtil =
            DataRepositoryTestUtil(
                repositoryProvider = repositoryProvider,
                repository = repositoryProvider.createFolderRepository()
            )
        getIsFolderLoadingStateUserCase =
            GetIsFolderLoadingStateUserCase(dataRepositoryTestUtil.repository)
    }

    @Test
    fun folderLoadingFlowTest() = runTest(dispatcher) {
        dataLoadingFlowTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            initDataList = initDataList,
            loadingFlow = getIsFolderLoadingStateUserCase.isLoading
        )
    }

    private val initDataList = List(20) { Folder(name = "test $it") }
}
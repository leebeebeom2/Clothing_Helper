package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.editContainerUseCaseTest
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditFolderNameUseCaseTest {
    private lateinit var editFolderNameUseCase: EditFolderNameUseCase
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
        editFolderNameUseCase =
            EditFolderNameUseCase(folderRepository = dataRepositoryTestUtil.repository)
    }

    @Test
    fun editFolderUseCaseTest() = runTest(dispatcher) {
        val folder = Folder(name = "test")

        editContainerUseCaseTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            addData = folder,
            edit = { oldData, name ->
                editFolderNameUseCase.editName(
                    oldFolder = oldData, name = name,
                    uid = dataRepositoryTestUtil.userRepositoryTestUtil.uid!!,
                    onFail = { assert(false) }
                )
            }
        )
    }
}
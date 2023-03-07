package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.addContainerUseCaseTest
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddFolderUseCaseTest {
    private lateinit var addFolderUseCase: AddFolderUseCase
    private lateinit var dataRepositoryTestUtil: DataRepositoryTestUtil<Folder, FolderRepository>
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        dataRepositoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider,
            repositoryProvider.createFolderRepository()
        )
        addFolderUseCase = AddFolderUseCase(dataRepositoryTestUtil.repository)
    }

    @Test
    fun addFolderUseCaseTest() = runTest(dispatcher) {
        addContainerUseCaseTest(
            dataRepositoryTestUtil = dataRepositoryTestUtil,
            add = {
                addFolderUseCase.add(
                    parentKey = "",
                    subCategoryKey = "",
                    name = it,
                    mainCategoryType = MainCategoryType.TOP,
                    uid = dataRepositoryTestUtil.userRepositoryTestUtil.uid!!,
                    onFail = { assert(false) }
                )
            }
        )
    }
}
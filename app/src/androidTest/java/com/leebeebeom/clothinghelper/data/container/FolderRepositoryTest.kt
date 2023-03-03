package com.leebeebeom.clothinghelper.data.container

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.data.repositorySignOutTest
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import org.junit.Before
import org.junit.Test

class FolderRepositoryTest {
    private lateinit var folderRepository: FolderRepository

    private val uid = "folder test"

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        folderRepository = RepositoryProvider.getFolderRepository(context)
    }

    @Test
    fun crudTest() {
        val folder = Folder("folder")

        repositoryCrudTest(
            type = Folder::class.java,
            data = folder,
            repository = folderRepository,
            uid = uid,
            addAssert = {
                assert(it.name == folder.name)
                assert(it.createDate == it.editDate)
            },
            newData = { it.copy(name = "new folder") }
        ) { origin, new ->
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
    fun signOutTest() {
        repositorySignOutTest(
            type = Folder::class.java,
            data1 = Folder("folder 1"),
            data2 = Folder("folder 2"),
            repository = folderRepository,
            uid = uid
        )
    }
}
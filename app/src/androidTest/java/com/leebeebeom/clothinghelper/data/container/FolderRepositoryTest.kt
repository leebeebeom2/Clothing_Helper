package com.leebeebeom.clothinghelper.data.container

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import org.junit.Before
import org.junit.Test

class FolderRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var folderRepository: FolderRepository

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        folderRepository = RepositoryProvider.getFolderRepository(context = context)
        userRepository = UserRepositoryImpl(appScope = RepositoryProvider.getAppScope())
    }

    @Test
    fun crudTest() {
        val folder = Folder("folder")

        repositoryCrudTest(
            userRepository = userRepository,
            data = folder,
            repository = folderRepository,
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
}
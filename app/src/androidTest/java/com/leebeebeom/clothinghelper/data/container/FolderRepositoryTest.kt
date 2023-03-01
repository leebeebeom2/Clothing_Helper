package com.leebeebeom.clothinghelper.data.container

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.data.repository.container.FolderRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferenceRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.data.repositoryCrudTest
import com.leebeebeom.clothinghelper.data.repositorySignOutTest
import com.leebeebeom.clothinghelper.domain.model.DatabaseFolder
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.toDatabaseModel
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.junit.Before
import org.junit.Test

class FolderRepositoryTest {
    private lateinit var networkPreferenceRepository: NetworkPreferenceRepository
    private lateinit var networkChecker: NetworkChecker
    private lateinit var folderPreferenceRepository: SortPreferenceRepository
    private lateinit var folderRepository: FolderRepository

    private val uid = "folder test"

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        networkPreferenceRepository = NetworkPreferenceRepositoryImpl(context = context)
        networkChecker = NetworkChecker(
            context = context, networkPreferenceRepository = networkPreferenceRepository
        )
        folderPreferenceRepository = FolderPreferencesRepositoryImpl(context = context)
        folderRepository = FolderRepositoryImpl(
            folderPreferencesRepository = folderPreferenceRepository,
            appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
            networkChecker = networkChecker
        )
    }

    @Test
    fun crudTest() {
        val folder = Folder("folder")

        repositoryCrudTest(
            data = folder.toDatabaseModel(),
            repository = folderRepository,
            uid = uid,
            type = DatabaseFolder::class.java,
            addAssert = {
                assert(it.name == folder.name)
                assert(it.createDate == it.editDate)
            },
            newData = { it.copy(name = "new folder") },
            editAssert = { origin, new ->
                assert(origin.key == new.key)
                assert(origin.name != new.name)
                assert(new.name == "new folder")
                assert(origin.key == new.key)
                assert(origin.mainCategoryType == new.mainCategoryType)
                assert(origin.createDate == new.createDate)
                assert(origin.editDate != new.editDate)
                assert(origin.editDate < new.editDate)
            }
        )
    }

    @Test
    fun signOutTest() {
        repositorySignOutTest(
            data1 = Folder("folder 1").toDatabaseModel(),
            data2 = Folder("folder 2").toDatabaseModel(),
            repository = folderRepository,
            uid = uid,
            type = DatabaseFolder::class.java
        )
    }
}
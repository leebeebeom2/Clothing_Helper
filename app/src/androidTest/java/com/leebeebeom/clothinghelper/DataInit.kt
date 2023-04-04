package com.leebeebeom.clothinghelper

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.repository.DataBasePath
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.container.FolderRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.container.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.waitTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataInit {
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(context = dispatcher)
    private val userRepository = UserRepositoryImpl(testScope, dispatcher)
    private val subCategoryPreferencesRepository =
        SubCategoryPreferencesRepositoryImpl(
            context = ApplicationProvider.getApplicationContext(),
            appScope = testScope
        )
    private val subCategoryRepository = SubCategoryRepositoryImpl(
        subCategoryPreferencesRepository = subCategoryPreferencesRepository,
        appScope = testScope,
        dispatcherIO = dispatcher,
        dispatcherDefault = dispatcher,
        userRepository = userRepository
    )
    private val folderPreferencesRepository = FolderPreferencesRepositoryImpl(
        context = ApplicationProvider.getApplicationContext(),
        appScope = testScope
    )
    private val folderRepository = FolderRepositoryImpl(
        folderPreferencesRepository = folderPreferencesRepository,
        testScope,
        dispatcherIO = dispatcher,
        dispatcherDefault = dispatcher,
        userRepository = userRepository
    )

    @Test
    fun init() = runTest(dispatcher) {
        Firebase.database.reference.child("Ggmv4fIeYhSviugZ1EqM7jpZ5dj2")
            .child(DataBasePath.SubCategory.path).removeValue().await()
        Firebase.database.reference.child("Ggmv4fIeYhSviugZ1EqM7jpZ5dj2")
            .child(DataBasePath.Folder.path).removeValue().await()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subCategoryRepository.allDataFlow.collect()
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            folderRepository.allDataFlow.collect()
        }

        waitTime()

        subCategoryRepository.init(dispatcher).awaitAll()

        waitTime()

        folderRepository.init(dispatcher, subCategoryRepository.allDataFlow.first().data).awaitAll()

        waitTime(5000)

        folderRepository.init2(dispatcher, folderRepository.allDataFlow.first().data).awaitAll()

        waitTime(10000)

        folderRepository.init3(dispatcher, folderRepository.allDataFlow.first().data).awaitAll()

        waitTime(10000)
    }
}
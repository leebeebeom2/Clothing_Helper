package com.leebeebeom.clothinghelper.data.repository

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.backgroundLaunch
import com.leebeebeom.clothinghelper.data.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.model.Folder
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FolderRepositoryTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)
    private val userRepository = UserRepositoryImpl(appScope = scope, dispatcherIO = dispatcher)
    private val folderRepository = FolderRepositoryImpl(
        folderPreferencesRepository = FolderPreferencesRepositoryImpl(
            context = ApplicationProvider.getApplicationContext(),
            appScope = scope
        ),
        appScope = scope,
        dispatcherIO = dispatcher,
        dispatcherDefault = dispatcher,
        userRepository = userRepository
    )

    @Before
    fun init() = runTest(dispatcher) {
        userRepository.signIn(RepositoryTestEmail, SignInPassword)
        backgroundLaunch { folderRepository.allDataFlow.collect() }
        waitTime()

        List(10) { parent ->
            List(parent + 1) { index ->
                Folder(
                    name = "parent $parent folder $index",
                    parentKey = "$parent"
                )
            }.map { async { folderRepository.add(it) } }
        }.flatten().awaitAll()
    }

    @After
    fun removeData() = runTest(dispatcher) {
        userRepository.signIn(RepositoryTestEmail, SignInPassword)
        backgroundLaunch { userRepository.userFlow.collect() }
        waitTime()

        Firebase.database.reference.child(userRepository.userFlow.first()!!.uid).removeValue()
    }

    @Test
    fun folderMapTest() = runTest(dispatcher) {
        backgroundLaunch { folderRepository.allDataFlow.collect() }
        backgroundLaunch { folderRepository.allFoldersMapFlow.collect() }
        backgroundLaunch { folderRepository.folderNamesMapFlow.collect() }
        backgroundLaunch { folderRepository.foldersSizeMapFlow.collect() }
        waitTime()

        val foldersMap = folderRepository.allFoldersMapFlow.first()
        val folderNamesMap = folderRepository.folderNamesMapFlow.first()
        val foldersSizeMap = folderRepository.foldersSizeMapFlow.first()

        foldersMap.data.forEach { (key, value) -> assert(value.size == key.toInt() + 1) }

        folderNamesMap.forEach { (key, value) -> assert(List(key.toInt() + 1) { "parent $key folder $it" }.toImmutableSet() == value) }

        foldersSizeMap.forEach { (key, value) -> assert((key.toInt() + 1) == value) }

        val key = 5.toString()
        val newFolder = Folder(name = "parent $key zAdded Folder", parentKey = key)
        folderRepository.add(newFolder)
        waitTime()

        val addedFolder =
            folderRepository.allFoldersMapFlow.first().data.getOrDefault(key, persistentListOf())
                .last()
        assert(addedFolder.name == newFolder.name)
        assert(addedFolder.parentKey == newFolder.parentKey)

        assert(
            folderRepository.folderNamesMapFlow.first().getOrDefault(key, persistentSetOf())
                .last() == newFolder.name
        )
        assert(folderRepository.foldersSizeMapFlow.first().getOrDefault(key, 0) == key.toInt() + 2)

        Firebase.database.reference.child(userRepository.userFlow.first()!!.uid)
            .child(DataBasePath.Folder.path)
            .child(addedFolder.key).removeValue().await()

        val foldersMap2 = folderRepository.allFoldersMapFlow.first()
        val folderNamesMap2 = folderRepository.folderNamesMapFlow.first()
        val foldersSizeMap2 = folderRepository.foldersSizeMapFlow.first()

        foldersMap2.data.forEach { (key, value) -> assert(value.size == key.toInt() + 1) }

        folderNamesMap2.forEach { (key, value) -> assert(List(key.toInt() + 1) { "parent $key folder $it" }.toImmutableSet() == value) }

        foldersSizeMap2.forEach { (key, value) -> assert((key.toInt() + 1) == value) }

        foldersMap2.data.forEach { (_, value) ->
            val deleteFolder = value.last().copy(deleted = true)
            folderRepository.push(deleteFolder)
        }

        waitTime()

        val foldersMap3 = folderRepository.allFoldersMapFlow.first()
        val folderNamesMap3 = folderRepository.folderNamesMapFlow.first()
        val foldersSizeMap3 = folderRepository.foldersSizeMapFlow.first()

        foldersMap3.data.forEach { (key, value) -> assert(value.size == key.toInt()) }

        folderNamesMap3.forEach { (key, value) -> assert(List(key.toInt()) { "parent $key folder $it" }.toImmutableSet() == value) }

        foldersSizeMap3.forEach { (key, value) -> assert((key.toInt()) == value) }
    }
}
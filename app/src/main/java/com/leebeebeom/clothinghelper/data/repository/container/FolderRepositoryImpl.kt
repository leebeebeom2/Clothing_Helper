package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.DataBasePath
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherDefault
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor(
    @FolderPreferencesRepository folderPreferencesRepository: SortPreferenceRepository,
    @AppScope appScope: CoroutineScope,
    @DispatcherIO dispatcherIO: CoroutineDispatcher,
    @DispatcherDefault dispatcherDefault: CoroutineDispatcher,
    userRepository: UserRepository,
) : BaseContainerRepositoryImpl<Folder, String>(
    sortFlow = folderPreferencesRepository.sortFlow,
    refPath = DataBasePath.Folder,
    appScope = appScope,
    type = Folder::class.java,
    dispatcherIO = dispatcherIO,
    dispatcherDefault = dispatcherDefault,
    userRepository = userRepository,
), FolderRepository {

    override fun groupByKeySelector(element: Folder) = element.parentKey

    suspend fun init(dispatcher: CoroutineDispatcher, subCategories: List<SubCategory>) =
        withContext(dispatcher) {
            subCategories.map { subCategory ->
                List(subCategory.name.last().digitToInt()) {
                    Folder(
                        name = "folder $it",
                        parentKey = subCategory.key,
                        subCategoryKey = subCategory.key,
                        mainCategoryType = subCategory.mainCategoryType
                    )
                }.map { async { add(it) } }
            }.flatten()
        }

    suspend fun init2(dispatcher: CoroutineDispatcher, folders: List<Folder>) =
        withContext(dispatcher) {
            folders.map { folder ->
                List(folder.name.last().digitToInt()) {
                    Folder(
                        name = "folder 1-$it",
                        parentKey = folder.key,
                        subCategoryKey = folder.subCategoryKey,
                        mainCategoryType = folder.mainCategoryType
                    )
                }.map { async { add(it) } }
            }.flatten()
        }

    suspend fun init3(dispatcher: CoroutineDispatcher, folders: List<Folder>) =
        withContext(dispatcher) {
            folders.filter { it.subCategoryKey != it.parentKey }.map { folder ->
                List(folder.name.last().digitToInt()) {
                    Folder(
                        name = "folder 2-$it",
                        parentKey = folder.key,
                        subCategoryKey = folder.subCategoryKey,
                        mainCategoryType = folder.mainCategoryType
                    )
                }.map { async { add(it) } }
            }.flatten()
        }
}
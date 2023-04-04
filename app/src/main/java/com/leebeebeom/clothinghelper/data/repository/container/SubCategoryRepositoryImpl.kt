package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.DataBasePath
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherDefault
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelper.ui.drawer.content.MainCategoryType
import com.leebeebeom.clothinghelper.ui.drawer.content.getMainCategories
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl @Inject constructor(
    @SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository,
    @AppScope appScope: CoroutineScope,
    @DispatcherIO dispatcherIO: CoroutineDispatcher,
    @DispatcherDefault dispatcherDefault: CoroutineDispatcher,
    userRepository: UserRepository,
) : BaseContainerRepositoryImpl<SubCategory, MainCategoryType>(
    sortFlow = subCategoryPreferencesRepository.sortFlow,
    refPath = DataBasePath.SubCategory,
    appScope = appScope,
    type = SubCategory::class.java,
    dispatcherIO = dispatcherIO,
    dispatcherDefault = dispatcherDefault,
    userRepository = userRepository
), SubCategoryRepository {

    override fun groupByKeySelector(element: SubCategory) = element.mainCategoryType

    suspend fun init(dispatcher: CoroutineDispatcher) =
        withContext(dispatcher) {
            var size = 10
            getMainCategories().map { mainCategory ->
                if (mainCategory.type == MainCategoryType.Etc) emptyList<SubCategory>().map { async { } }
                else List(size--) {
                    SubCategory(
                        name = "subCategory $it", mainCategoryType = mainCategory.type
                    )
                }.map { async { add(it) } }
            }.flatten()
        }
}

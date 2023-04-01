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
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl @Inject constructor(
    @SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository,
    @AppScope appScope: CoroutineScope,
    @DispatcherIO dispatcherIO: CoroutineDispatcher,
    @DispatcherDefault dispatcherDefault: CoroutineDispatcher,
    userRepository: UserRepository,
) : BaseContainerRepositoryImpl<SubCategory>(
    sortFlow = subCategoryPreferencesRepository.sortFlow,
    refPath = DataBasePath.SubCategory,
    appScope = appScope,
    type = SubCategory::class.java,
    dispatcherIO = dispatcherIO,
    dispatcherDefault = dispatcherDefault,
    userRepository = userRepository
), SubCategoryRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val subCategorySizeMapFlow = allDataFlow.mapLatest {
        it.data.groupingBy { element -> element.mainCategoryType }.eachCount().toImmutableMap()
    }.flowOn(dispatcherDefault)
        .distinctUntilChanged()
        .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val subCategoryNamesMapFlow =
        allDataFlow.mapLatest {
            it.data.groupBy { element -> element.mainCategoryType }
                .mapValues { mapElement ->
                    mapElement.value.map { element -> element.name }.toImmutableSet()
                }
                .toImmutableMap()
        }.flowOn(dispatcherDefault)
            .distinctUntilChanged()
            .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)
}

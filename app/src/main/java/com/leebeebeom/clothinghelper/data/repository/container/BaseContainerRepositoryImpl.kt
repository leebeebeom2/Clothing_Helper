package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.BaseDataRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.Order.ASCENDING
import com.leebeebeom.clothinghelper.data.repository.preference.Order.DESCENDING
import com.leebeebeom.clothinghelper.data.repository.preference.Sort.*
import com.leebeebeom.clothinghelper.data.repository.preference.SortPreferences
import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*


@Suppress("UNCHECKED_CAST")
abstract class BaseContainerRepositoryImpl<T : BaseContainerModel>(
    sortFlow: Flow<SortPreferences>,
    refPath: String,
    appScope: CoroutineScope,
    type: Class<T>,
    dispatcher: CoroutineDispatcher,
    userRepository: UserRepository,
) : BaseDataRepository<T>, BaseDataRepositoryImpl<T>(
    refPath = refPath,
    appScope = appScope,
    type = type,
    dispatcher = dispatcher,
    userRepository = userRepository
) {
    override val allData =
        super.allData.combine(flow = sortFlow, transform = ::getSortedData).shareIn(
            scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1
        )

    private fun getSortedData(
        allData: List<T>,
        sortPreferences: SortPreferences,
    ): List<T> {
        val sort = sortPreferences.sort
        val order = sortPreferences.order

        return when {
            sort == NAME && order == ASCENDING -> allData.sortedBy { it.name }
            sort == NAME && order == DESCENDING -> allData.sortedByDescending { it.name }
            sort == CREATE && order == ASCENDING -> allData.sortedBy { it.createDate }
            sort == CREATE && order == DESCENDING -> allData.sortedByDescending { it.createDate }
            sort == EDIT && order == ASCENDING -> allData.sortedBy { it.editDate }
            sort == EDIT && order == DESCENDING -> allData.sortedByDescending { it.editDate }
            else -> allData
        }
    }

    override suspend fun push(data: T) = super.push(data = data.changeEditDate() as T)
}
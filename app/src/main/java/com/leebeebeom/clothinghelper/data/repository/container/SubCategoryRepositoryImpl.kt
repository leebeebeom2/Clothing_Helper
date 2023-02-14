package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.data.repository.util.DatabaseCallSite
import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl @Inject constructor(@SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository) :
    BaseDataRepositoryImpl<SubCategory>(
        sortFlow = subCategoryPreferencesRepository.sort,
        refPath = DatabasePath.SUB_CATEGORIES
    ), SubCategoryRepository {

    override suspend fun pushInitialSubCategories(uid: String) =
        databaseTryWithLoading(DatabaseCallSite("SubCategory: pushInitialSubCategories")) {
            withContext(Dispatchers.IO) {
                val subCategoryRef = root.getContainerRef(
                    uid = uid,
                    path = DatabasePath.SUB_CATEGORIES
                )
                getInitialSubCategories().forEach {
                    val subCategoryWithKey = it.copy(key = getKey(subCategoryRef))
                    val result = async { push(subCategoryRef, subCategoryWithKey) }
                    result.await()
                }
                FirebaseResult.Success
            }
        }

    companion object {
        fun getInitialSubCategories(): List<SubCategory> {
            var timeStamp = System.currentTimeMillis()
            return listOf(
                SubCategory(
                    parent = MainCategoryType.TOP,
                    name = "반팔",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.TOP,
                    name = "긴팔",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.TOP,
                    name = "셔츠",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.TOP,
                    name = "반팔 셔츠",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.TOP,
                    name = "니트",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.TOP,
                    name = "반팔 니트",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.TOP,
                    name = "니트 베스트",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.BOTTOM,
                    name = "데님",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.BOTTOM,
                    name = "반바지",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.BOTTOM,
                    name = "슬랙스",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.BOTTOM,
                    name = "스웻",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.BOTTOM,
                    name = "나일론",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.BOTTOM,
                    name = "치노",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.OUTER,
                    name = "코트",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.OUTER,
                    name = "자켓",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.OUTER,
                    name = "바람막이",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.OUTER,
                    name = "항공점퍼",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.OUTER,
                    name = "블루종",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.OUTER,
                    name = "점퍼",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.OUTER,
                    name = "야상",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.ETC,
                    name = "신발",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.ETC,
                    name = "목걸이",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.ETC,
                    name = "팔찌",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.ETC,
                    name = "귀걸이",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.ETC,
                    name = "볼캡",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.ETC,
                    name = "비니",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.ETC,
                    name = "머플러",
                    createDate = timeStamp++,
                    editDate = timeStamp
                ), SubCategory(
                    parent = MainCategoryType.ETC,
                    name = "장갑",
                    createDate = timeStamp,
                    editDate = timeStamp
                )
            )
        }
    }
}

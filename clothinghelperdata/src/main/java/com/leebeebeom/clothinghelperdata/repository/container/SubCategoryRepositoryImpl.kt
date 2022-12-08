package com.leebeebeom.clothinghelperdata.repository.container

import com.leebeebeom.clothinghelperdata.repository.base.DatabasePath
import com.leebeebeom.clothinghelperdomain.model.data.SubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.BaseDataRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.repository.preferences.SortPreferenceRepository
import com.leebeebeom.clothinghelperdomain.repository.preferences.SubCategoryPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl(private val baseDataRepositoryImpl: BaseDataRepositoryImpl<SubCategory>) :
    BaseDataRepository<SubCategory> by baseDataRepositoryImpl, SubCategoryRepository {

    @Inject
    constructor(
        @SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository
    ) : this(
        baseDataRepositoryImpl = BaseDataRepositoryImpl(
            sortFlow = subCategoryPreferencesRepository.sort,
            refPath = DatabasePath.SUB_CATEGORIES
        )
    )

    override suspend fun pushInitialSubCategories(uid: String) = withContext(Dispatchers.IO) {
        val subCategoryRef = baseDataRepositoryImpl.root.getContainerRef(
            uid = uid,
            path = DatabasePath.SUB_CATEGORIES
        )

        getInitialSubCategories().forEach {
            val subCategoryWithKey = it.copy(key = baseDataRepositoryImpl.getKey(subCategoryRef))
            val result = async { baseDataRepositoryImpl.push(subCategoryRef, subCategoryWithKey) }
            result.await()
        }
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()
        return listOf(
            SubCategory(
                parent = SubCategoryParent.TOP,
                name = "반팔",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "긴팔",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "셔츠",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "반팔 셔츠",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "니트",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "반팔 니트",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.TOP,
                name = "니트 베스트",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "데님",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "반바지",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "슬랙스",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "스웻",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "나일론",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM,
                name = "치노",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "코트",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "자켓",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "바람막이",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "항공점퍼",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "블루종",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "점퍼",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.OUTER,
                name = "야상",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "신발",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "목걸이",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "팔찌",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "귀걸이",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "볼캡",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "비니",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "머플러",
                createDate = timeStamp++,
                editDate = timeStamp
            ), SubCategory(
                parent = SubCategoryParent.ETC,
                name = "장갑",
                createDate = timeStamp,
                editDate = timeStamp
            )
        )
    }
}

package com.leebeebeom.clothinghelperdata.repository.base

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl @Inject constructor() : BaseContainerRepository<SubCategory>(),
    SubCategoryRepository {

    override val allSubCategories get() = allContainers.asStateFlow()

    override suspend fun updateSubCategories(uid: String): FirebaseResult {
        return load(uid, SubCategory::class.java)
    }

    override suspend fun addSubCategory(subCategory: SubCategory): FirebaseResult {
        return add(subCategory)
    }

    override suspend fun editSubCategoryName(newSubCategory: SubCategory): FirebaseResult {
        return edit(newSubCategory)
    }

    override suspend fun pushInitialSubCategories(uid: String) {
        return withContext(Dispatchers.IO) {
            val subCategoryRef = root.getContainerRef(uid, refPath)

            getInitialSubCategories().forEach {
                val subCategoryWithKey = getContainerWithKey(it, getKey(subCategoryRef))
                val result = async { push(subCategoryRef, subCategoryWithKey) }
                result.await()
            }

            FirebaseResult.Success
        }
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()
        return listOf(
            SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "긴팔", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "셔츠", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔 셔츠", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "니트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔 니트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "니트 베스트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "데님", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "반바지", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "슬랙스", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "스웻", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "나일론", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "치노", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "코트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "자켓", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "바람막이", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "항공점퍼", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "블루종", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "점퍼", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "야상", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "신발", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "목걸이", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "팔찌", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "귀걸이", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "볼캡", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "비니", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "머플러", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "장갑", createDate = timeStamp
            )
        )
    }

    override val refPath = DatabasePath.SUB_CATEGORIES

    override fun getContainerWithKey(
        value: SubCategory,
        key: String,
        createDate: Long
    ): SubCategory {
        return value.copy(key = key, createDate = createDate)
    }

    override fun getContainerWithKey(value: SubCategory, key: String): SubCategory {
        return value.copy(key = key)
    }
}

fun <T> MutableStateFlow<List<T>>.updateMutable(task: (MutableList<T>) -> Unit) {
    val temp = value.toMutableList()
    task(temp)
    update { temp }
}
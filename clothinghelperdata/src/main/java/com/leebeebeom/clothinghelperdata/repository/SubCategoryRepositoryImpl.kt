package com.leebeebeom.clothinghelperdata.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelperdata.repository.base.BaseRepository
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.util.logE
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl @Inject constructor() : BaseRepository(true),
    SubCategoryRepository {
    private val root = Firebase.database.reference

    private val _allSubCategories = MutableStateFlow(List(4) { emptyList<SubCategory>() })
    override val allSubCategories get() = _allSubCategories.asStateFlow()

    private var uid: String = ""

    override suspend fun updateSubCategories(uid: String): FirebaseResult {
        return firebaseTry(3000, "updateSubCategories") {
            this@SubCategoryRepositoryImpl.uid = uid

            val temp = MutableList(4) { mutableListOf<SubCategory>() }

            root.getSubCategoriesRef(uid).get().await().children.forEach { data ->
                val subCategory = data.getValue(SubCategory::class.java)!!
                temp[subCategory.parent.ordinal].add(subCategory)
            }

            _allSubCategories.update { temp }
            FirebaseResult.Success
        }
    }

    override suspend fun pushInitialSubCategories(uid: String) {
        return withContext(Dispatchers.IO) {
            val subCategoryRef = root.getSubCategoriesRef(uid)

            for (subCategory in getInitialSubCategories()) {
                val subCategoryWithKey = getSubCategoryWithKey(subCategoryRef, subCategory)
                val result = async { pushSubCategory(subCategoryRef, subCategoryWithKey) }
                result.await()
            }
            FirebaseResult.Success
        }
    }

    private fun getSubCategoryWithKey(
        subCategoryRef: DatabaseReference, subCategory: SubCategory
    ): SubCategory {
        val key = subCategoryRef.push().key!!
        return subCategory.copy(key = key)
    }

    private suspend fun pushSubCategory(
        subCategoryRef: DatabaseReference, subCategory: SubCategory
    ): Task<Void> {
        return withContext(Dispatchers.IO) {
            subCategoryRef.child(subCategory.key).setValue(subCategory)
        }
    }

    override suspend fun addSubCategory(subCategory: SubCategory): FirebaseResult {
        return firebaseTryWithOutLoading(1000, "addSubCategory") {
            val subCategoryRef = root.getSubCategoriesRef(uid)

            val subCategoryWithKey =
                getSubCategoryWithKey(
                    subCategoryRef,
                    subCategory
                ).copy(createDate = System.currentTimeMillis())

            pushSubCategory(subCategoryRef, subCategoryWithKey).await()
            _allSubCategories.singleListUpdate(subCategory.parent.ordinal) {
                it.add(subCategoryWithKey)
            }
            FirebaseResult.Success
        }
    }

    override suspend fun editSubCategoryName(newSubCategory: SubCategory): FirebaseResult {
        return firebaseTryWithOutLoading(1000, "editSubCategoryName") {
            root.getSubCategoriesRef(uid).child(newSubCategory.key).setValue(newSubCategory).await()
            _allSubCategories.singleListUpdate(newSubCategory.parent.ordinal) {
                val oldSubCategory =
                    it.first { subCategory -> subCategory.key == newSubCategory.key }
                it.remove(oldSubCategory)
                it.add(newSubCategory)
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

    private fun DatabaseReference.getSubCategoriesRef(uid: String): DatabaseReference {
        return child(uid).child(DatabasePath.SUB_CATEGORIES)
    }

    private suspend fun firebaseTryWithOutLoading(
        time: Long,
        site: String,
        task: suspend () -> FirebaseResult
    ): FirebaseResult {
        return withContext(Dispatchers.IO) {
            try {
                withTimeout(time) { task() }
            } catch (e: TimeoutCancellationException) {
                logE(site, e)
                FirebaseResult.Fail(e)
            } catch (e: Exception) {
                logE(site, e)
                FirebaseResult.Fail(e)
            }
        }
    }

    private suspend fun firebaseTry(
        time: Long,
        site: String,
        task: suspend () -> FirebaseResult
    ): FirebaseResult {
        return withContext(Dispatchers.IO) {
            try {
                loadingOn()
                withTimeout(time) { task() }
            } catch (e: TimeoutCancellationException) {
                logE(site, e)
                FirebaseResult.Fail(e)
            } catch (e: Exception) {
                logE(site, e)
                FirebaseResult.Fail(e)
            } finally {
                loadingOff()
            }
        }
    }
}

object DatabasePath {
    const val SUB_CATEGORIES = "sub categories"
    const val USER_INFO = "user info"
}

fun MutableStateFlow<List<List<SubCategory>>>.singleListUpdate(
    index: Int, task: (MutableList<SubCategory>) -> Unit
) {
    val temp = value.toMutableList()
    val temp2 = temp[index].toMutableList()
    task(temp2)
    temp[index] = temp2
    update { temp }
}
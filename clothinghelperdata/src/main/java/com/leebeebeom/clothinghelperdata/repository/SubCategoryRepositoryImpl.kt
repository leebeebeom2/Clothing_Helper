package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow

class SubCategoryRepositoryImpl(private val userRepository: UserRepository) :
    SubCategoryRepository {
    private val root = Firebase.database.reference
    private val user = MutableStateFlow(userRepository.user.value)

    private val _topSubCategories = MutableStateFlow(emptyList<SubCategory>())
    override val topSubCategories get() = _topSubCategories
    private val _bottomSubCategories = MutableStateFlow(emptyList<SubCategory>())
    override val bottomSubCategories get() = _bottomSubCategories
    private val _outerSubCategories = MutableStateFlow(emptyList<SubCategory>())
    override val outerSubCategories get() = _outerSubCategories
    private val _etcSubCategories = MutableStateFlow(emptyList<SubCategory>())
    override val etcSubCategories get() = _etcSubCategories

    override suspend fun loadSubCategories(
        onDone: List<() -> Unit>,
        onCancelled: List<(Int, String) -> Unit>
    ) {
        userRepository.user.collect {
            this.user.value = it
            it?.let {
                root.addSingleValueListener(
                    uid = it.uid,
                    subCategories = topSubCategories,
                    subCategoryParent = SubCategoryParent.TOP,
                    onDone = onDone[0],
                    onCancelled = onCancelled[0]
                )
                root.addSingleValueListener(
                    uid = it.uid,
                    subCategories = bottomSubCategories,
                    subCategoryParent = SubCategoryParent.BOTTOM,
                    onDone = onDone[1],
                    onCancelled = onCancelled[1]
                )
                root.addSingleValueListener(
                    uid = it.uid,
                    subCategories = outerSubCategories,
                    subCategoryParent = SubCategoryParent.OUTER,
                    onDone = onDone[2],
                    onCancelled = onCancelled[2]
                )
                root.addSingleValueListener(
                    uid = it.uid,
                    subCategories = etcSubCategories,
                    subCategoryParent = SubCategoryParent.ETC,
                    onDone = onDone[3],
                    onCancelled = onCancelled[3]
                )
            }
        }

    }

    override fun writeInitialSubCategory(uid: String) {
        root.getSubCategoriesRef(uid)
            .setValue(getInitialSubCategories()) // TODO 10000개로 테스트 해보기
    }

    override fun addSubCategory(
        subCategoryParent: SubCategoryParent,
        name: String,
        addSubCategoryListener: FirebaseListener
    ) {
        val timeStamp = System.currentTimeMillis()
        val newSubCategory = SubCategory(subCategoryParent, timeStamp, name)

        root.getSubCategoriesRef(user.value!!.uid).push().setValue(newSubCategory)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    addSubCategory(subCategoryParent, newSubCategory)
                    addSubCategoryListener.taskSuccess()
                } else addSubCategoryListener.taskFailed(it.exception)
            }
    }

    private fun addSubCategory(subCategoryParent: SubCategoryParent, newSubCategory: SubCategory) {
        when (subCategoryParent) {
            SubCategoryParent.TOP -> _topSubCategories.value =
                _topSubCategories.value.addAndReturn(newSubCategory)
            SubCategoryParent.BOTTOM -> _bottomSubCategories.value =
                _bottomSubCategories.value.addAndReturn(newSubCategory)
            SubCategoryParent.OUTER -> _outerSubCategories.value =
                _outerSubCategories.value.addAndReturn(newSubCategory)
            SubCategoryParent.ETC -> _etcSubCategories.value =
                _etcSubCategories.value.addAndReturn(newSubCategory)
        }
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()

        return listOf(
            SubCategory(
                SubCategoryParent.TOP, timeStamp++, "반팔"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "긴팔"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "셔츠"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "반팔 셔츠"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "니트"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "반팔 니트"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "니트 베스트"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "데님"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "반바지"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "슬랙스"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "스웻"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "나일론"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "치노"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "코트"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "자켓"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "바람막이"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "항공점퍼"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "블루종"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "점퍼"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "야상"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "신발"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "목걸이"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "팔찌"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "귀걸이"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "볼캡"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "비니"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "머플러"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp, "장갑"
            )
        )
    }

    private fun DatabaseReference.getUidRef(uid: String) = child(uid)
    private fun DatabaseReference.getSubCategoriesRef(uid: String) =
        getUidRef(uid).child(DatabasePath.SUB_CATEGORIES)

    private fun DatabaseReference.addSingleValueListener(
        uid: String,
        subCategoryParent: SubCategoryParent,
        subCategories: MutableStateFlow<List<SubCategory>>,
        onDone: () -> Unit,
        onCancelled: (Int, String) -> Unit
    ) {
        getSubCategoriesRef(uid).orderByChild("parent").equalTo(subCategoryParent.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val temp = mutableListOf<SubCategory>()
                    for (child in snapshot.children) {
                        child.getValue<SubCategory>()?.let { temp.add(it) }
                    }
                    subCategories.value = temp
                    onDone()
                }

                override fun onCancelled(error: DatabaseError) =
                    onCancelled(error.code, error.message)
            })
    }
}

object DatabasePath {
    const val SUB_CATEGORIES = "sub categories"
    const val USER_INFO = "user info"
}

fun <T> List<T>.addAndReturn(value: T): List<T> {
    val mutableList = toMutableList()
    mutableList.add(value)
    return mutableList
}
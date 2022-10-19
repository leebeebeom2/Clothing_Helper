package com.leebeebeom.clothinghelper.data.datasource

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.model.SubCategory
import com.leebeebeom.clothinghelper.data.model.SubCategoryParent
import com.leebeebeom.clothinghelper.data.model.User
import com.leebeebeom.clothinghelper.domain.usecase.user.UserInfoUserCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// https://firebase.google.com/docs/database/android/read-and-write -> 기본 쓰기 작업: 데이터 일부만 수정

class SubCategoryRemoteDataSource(private val userInfoUserCase: UserInfoUserCase) {
    private val userRef get() = Firebase.database.reference.child(userInfoUserCase.uid.value)
    private val subCategoryRef get() = userRef.child("subCategory")
    private var _allSubCategories = MutableStateFlow(emptyList<SubCategory>())
    val allSubCategories: StateFlow<List<SubCategory>> get() = _allSubCategories
    private var _topSubCategories = MutableStateFlow(emptyList<SubCategory>())
    val topSubCategories: StateFlow<List<SubCategory>> get() = _allSubCategories
    private var _bottomSubCategories = MutableStateFlow(emptyList<SubCategory>())
    val bottomSubCategories: StateFlow<List<SubCategory>> get() = _allSubCategories
    private var _outerSubCategories = MutableStateFlow(emptyList<SubCategory>())
    val outerSubCategories: StateFlow<List<SubCategory>> get() = _allSubCategories
    private var _etcSubCategories = MutableStateFlow(emptyList<SubCategory>())
    val etcSubCategories: StateFlow<List<SubCategory>> get() = _allSubCategories

    @Suppress("UNCHECKED_CAST")
    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            _allSubCategories.value = snapshot.value as? List<SubCategory> ?: emptyList()
            if (allSubCategories.value.isNotEmpty()) {
                val subCategoryMap = allSubCategories.value.groupBy { it.parent }
                _topSubCategories.value =
                    subCategoryMap.getOrElse(SubCategoryParent.Top) { emptyList() }
                _bottomSubCategories.value =
                    subCategoryMap.getOrElse(SubCategoryParent.Bottom) { emptyList() }
                _outerSubCategories.value =
                    subCategoryMap.getOrElse(SubCategoryParent.OUTER) { emptyList() }
                _etcSubCategories.value =
                    subCategoryMap.getOrElse(SubCategoryParent.ETC) { emptyList() }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            throw Exception("서브 카테고리 가져오기 실패")
        }
    }

    init {
        subCategoryRef.addValueEventListener(valueEventListener)
    }

    fun writeInitialData() {
        val user = User(userInfoUserCase.email.value, userInfoUserCase.name.value)
        userRef.child("user info").setValue(user)
        subCategoryRef.setValue(getInitialSubCategories())
    }

    fun addSubCategory(parent: SubCategoryParent, name: String) {
        val timeStamp = System.currentTimeMillis()
        val newSubCategory = SubCategory(parent, timeStamp, name)

        subCategoryRef.push().setValue(newSubCategory)
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()

        return listOf(
            SubCategory(SubCategoryParent.Top, timeStamp++, "반팔"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "긴팔"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "셔츠"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "반팔 셔츠"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "니트"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "반팔 니트"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "니트 베스트"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "데님"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "반바지"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "슬랙스"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "스웻"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "나일론"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "치노"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "코트"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "자켓"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "바람막이"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "항공점퍼"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "블루종"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "점퍼"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "야상"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "신발"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "목걸이"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "팔찌"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "귀걸이"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "볼캡"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "비니"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "머플러"),
            SubCategory(SubCategoryParent.ETC, timeStamp, "장갑")
        )
    }
}
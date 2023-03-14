package com.leebeebeom.clothinghelper.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

const val toastTextKey = "toastText"

@HiltViewModel
class ActivityViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val toastTestList = savedStateHandle.getStateFlow(toastTextKey, emptyList<Int>())
    private val user =
        getUserUseCase.getUser(onFail = { showToast(R.string.error_fail_get_user_info_by_unknow_error) })

    val activityUiState = combine(
        flow = toastTestList,
        flow2 = user,
    ) { toastText, userResult ->
        ActivityUiState(toastText = toastText, user = userResult)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ActivityUiState())

    fun showToast(toastText: Int) {
        val mutableToastTextList = toastTestList.value.toMutableList()
        mutableToastTextList.add(toastText)
        savedStateHandle[toastTextKey] = mutableToastTextList
    }

    fun toastShown() {
        val mutableToastTextList = toastTestList.value.toMutableList()
        mutableToastTextList.removeAt(0)
        savedStateHandle[toastTextKey] = mutableToastTextList
    }
}

data class ActivityUiState(
    val toastText: List<Int> = emptyList(),
    val user: User? = null,
)

@Composable
fun activityViewModel(context: Context = LocalContext.current): ActivityViewModel =
    hiltViewModel(viewModelStoreOwner = (context as ComponentActivity))
package com.leebeebeom.clothinghelper.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

const val ToastTextKey = "toastText"

@HiltViewModel
class ActivityViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val activityUiState = combine(
        flow = savedStateHandle.getStateFlow(key = ToastTextKey, initialValue = emptyList<Int>()),
        flow2 = getUserUseCase.userStream,
    ) { toastTexts, user ->
        ActivityUiState(toastTexts = toastTexts, user = user)
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000),
        initialValue = ActivityUiState()
    )

    fun addToastTextAtLast(toastText: Int) {
        val mutableToastTextList = activityUiState.value.toastTexts.toMutableList()
        mutableToastTextList.add(toastText)
        savedStateHandle[ToastTextKey] = mutableToastTextList
    }

    fun removeFirstToastText() {
        val mutableToastTextList = activityUiState.value.toastTexts.toMutableList()
        mutableToastTextList.removeAt(0)
        savedStateHandle[ToastTextKey] = mutableToastTextList
    }
}

data class ActivityUiState(
    val toastTexts: List<Int> = emptyList(),
    val user: User? = null,
)

@Composable
fun activityViewModel(context: Context = LocalContext.current): ActivityViewModel =
    hiltViewModel(viewModelStoreOwner = (context as ComponentActivity))
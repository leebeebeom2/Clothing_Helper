package com.leebeebeom.clothinghelper.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(getUserUseCase: GetUserUseCase) : ViewModel() {
    private val toastTexts = mutableStateListOf<Int>()

    val activityUiState = combine(
        flow = snapshotFlow { toastTexts }, flow2 = getUserUseCase.userStream
    ) { toastTexts, user ->
        ActivityUiState(toastTexts = toastTexts.toList(), user = user)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ActivityUiState()
    )

    fun addToastTextAtLast(toastText: Int) {
        toastTexts.add(toastText)
    }

    fun removeFirstToastText() {
        toastTexts.removeFirst()
    }
}

data class ActivityUiState(
    val toastTexts: List<Int> = emptyList(),
    val user: User? = null,
)

@Composable
fun activityViewModel(context: Context = LocalContext.current): ActivityViewModel =
    hiltViewModel(viewModelStoreOwner = (context as ComponentActivity))
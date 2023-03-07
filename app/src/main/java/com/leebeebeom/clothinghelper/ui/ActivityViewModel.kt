package com.leebeebeom.clothinghelper.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(getUserUseCase: GetUserUseCase) :
    ViewModel() {

    private val _toastTest: MutableStateFlow<Int?> = MutableStateFlow(null)

    val activityUiState = combine(
        flow = _toastTest,
        flow2 = getUserUseCase.user
    ) { toastText, user ->
        ActivityUiState(
            toastText = toastText,
            user = user
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ActivityUiState()
    )

    fun showToast(toastText: Int) {
        _toastTest.value = toastText
    }

    fun toastShown() {
        _toastTest.value = null
    }
}

data class ActivityUiState(
    val toastText: Int? = null,
    val user: User? = null,
)

@Composable
fun activityViewModel(context: Context = LocalContext.current): ActivityViewModel =
    hiltViewModel(viewModelStoreOwner = (context as ComponentActivity))
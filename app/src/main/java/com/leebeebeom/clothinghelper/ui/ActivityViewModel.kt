package com.leebeebeom.clothinghelper.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.domain.usecase.user.GetSignInStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val getSignInStateUseCase: GetSignInStateUseCase) :
    ViewModel() {
    private val _activityUiState = MutableActivityUiState()
    val activityUiState: ActivityUiState = _activityUiState

    fun showToast(toastText: Int) {
        _activityUiState.toastText = toastText
    }

    fun toastShown() {
        _activityUiState.toastText = null
    }

    val isSignIn get() = getSignInStateUseCase.isSignIn
}

@Stable
interface ActivityUiState {
    val toastText: Int?
}

private class MutableActivityUiState : ActivityUiState {
    override var toastText: Int? by mutableStateOf(null)
}

@Composable
fun activityViewModel(context: Context = LocalContext.current): ActivityViewModel =
    hiltViewModel(viewModelStoreOwner = (context as ComponentActivity))
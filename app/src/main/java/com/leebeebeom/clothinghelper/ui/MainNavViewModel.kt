package com.leebeebeom.clothinghelper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import com.leebeebeom.clothinghelper.ui.state.LoadingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainNavViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase
) : ViewModel() {

    val uiState = getUserUseCase.userFlow.mapLatest {
        MainNavUiState(user = it, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainNavUiState(user = getUserUseCase.getUser())
    )
}

data class MainNavUiState(
    val user: User? = null, override val isLoading: Boolean = true
) : LoadingUiState
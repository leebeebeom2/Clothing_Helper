package com.leebeebeom.clothinghelper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(getUserUseCase: GetUserUseCase) :
    ViewModel() {
    val userFlow =
        getUserUseCase.userFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = getUserUseCase.getUser()
        )
}
package com.leebeebeom.clothinghelper.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface LoadingStateProvider {
    val isLoading: StateFlow<Boolean>
}
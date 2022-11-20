package com.leebeebeom.clothinghelperdomain.repository

import kotlinx.coroutines.flow.StateFlow

interface LoadingRepository {
    val isLoading: StateFlow<Boolean>
}
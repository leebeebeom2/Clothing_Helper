package com.leebeebeom.clothinghelper.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.base.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetSignInLoadingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun SignInRoot(
    viewModel: SignInRootViewModel = hiltViewModel(),
    focusManager: FocusManager = LocalFocusManager.current,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable (PaddingValues) -> Unit
) {
    ClothingHelperTheme {
        Scaffold(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clickable(
                    interactionSource = interactionSource, indication = null
                ) { focusManager.clearFocus() }
                .verticalScroll(rememberScrollState()),
            content = content
        )
    }

    if (viewModel.isLoading) CenterDotProgressIndicator()
}

@HiltViewModel
class SignInRootViewModel @Inject constructor(private val getSignInLoadingStateUseCase: GetSignInLoadingStateUseCase) :
    ViewModel() {
    var isLoading by mutableStateOf(false)

    init {
        viewModelScope.launch {
            getSignInLoadingStateUseCase().collect {
                isLoading = it
            }
        }
    }
}
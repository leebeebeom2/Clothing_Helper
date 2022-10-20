package com.leebeebeom.clothinghelper.ui

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.ui.main.base.MainNavHost
import com.leebeebeom.clothinghelper.ui.signin.SignInNavHost
import com.leebeebeom.clothinghelperdomain.usecase.user.GetLoginStateUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "TAG"

@HiltAndroidApp
class ClothingHelper : Application()


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityNavHost() }
    }
}

@Composable
fun MainActivityNavHost(viewModel: MainActivityViewModel = hiltViewModel()) {
    if (viewModel.isLogin) MainNavHost()
    else SignInNavHost()
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getLoginStateUseCase: GetLoginStateUseCase
) : ViewModel() {

    var isLogin by mutableStateOf(getLoginStateUseCase.isLogin.value)
        private set

    init {
        viewModelScope.launch {
            getLoginStateUseCase.isLogin.collect {
                isLogin = it
            }
        }
    }
}
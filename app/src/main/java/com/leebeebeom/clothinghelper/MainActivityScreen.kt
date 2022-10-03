package com.leebeebeom.clothinghelper

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme

@Composable
fun MainActivityScreen() {
    ClothingHelperTheme {
        Scaffold(
            drawerContent = {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                Divider()
            },
            bottomBar = { CHBottomAppBar() },
            floatingActionButton = { CHFab() },
            isFloatingActionButtonDocked = true
        ) {
        }
    }
}

@Composable
fun CHBottomAppBar() {
    BottomAppBar {

    }
}

@Composable
fun CHFab() {
    FloatingActionButton(onClick = { /*TODO*/ }) {

    }
}
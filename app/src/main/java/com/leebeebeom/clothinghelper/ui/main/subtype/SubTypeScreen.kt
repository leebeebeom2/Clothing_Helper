package com.leebeebeom.clothinghelper.ui.main.subtype

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleIcon

@Composable
fun SubTypeScreen(viewModel: SubTypeViewModel = viewModel()) {

    Box {
        Scaffold(floatingActionButton = { AddFab() }) {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(viewModel.subTypes) { subType ->
                    Text(text = subType)
                }
            }
        }
    }
}

@Composable
fun AddFab() {
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(onClick = { showDialog = true }
    ) { SimpleIcon(drawableId = R.drawable.ic_add) }

    if (showDialog) AddCategoryDialog {
        showDialog = false
    }
}

@Composable
fun AddCategoryDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .wrapContentHeight()
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column {
                    Text(text = "카테고리 추가", style = MaterialTheme.typography.subtitle1)
                    SimpleHeightSpacer(dp = 12)

                    val focusRequester = remember { FocusRequester() }
                    var text by remember { mutableStateOf("") }
                    Surface {
                        OutlinedTextField(
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .padding(bottom = 40.dp),
                            value = text, onValueChange = { text = it },
                            label = { Text(text = "카테고리") },
                            placeholder = { Text(text = "ex) 반팔, 긴팔, 셔츠") }
                        )
                    }

                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddCategoryDialogPreview() {
    AddCategoryDialog {

    }
}
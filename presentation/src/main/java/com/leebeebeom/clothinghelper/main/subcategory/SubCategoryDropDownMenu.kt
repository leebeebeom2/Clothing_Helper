package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R

@Composable
fun SubCategoryDropDownMenu(
    showDropDownMenu: Boolean,
    onDismiss: () -> Unit,
    deletedSubCategory: () -> Unit
) {
    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(20.dp))) {
        DropdownMenu(
            expanded = showDropDownMenu,
            onDismissRequest = onDismiss,
            offset = DpOffset(30.dp, (-30).dp)
        ) {
            DropDownMenuText(text = R.string.select_mode) {
                onDismiss()
            }
            DropDownMenuText(text = R.string.change_name) {
                onDismiss()
            }
            DropDownMenuText(text = R.string.delete) {
                deletedSubCategory()
                onDismiss()
            }
        }
    }
}

@Composable
fun DropDownMenuText(@StringRes text: Int, onClick: () -> Unit) {
    Text(
        text = stringResource(id = text),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(start = 8.dp, end = 24.dp)
            .padding(vertical = 8.dp)
    )
}
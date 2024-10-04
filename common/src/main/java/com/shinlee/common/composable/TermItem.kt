package com.shinlee.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.shinlee.common.theme.PinkColor
import com.shinlee.common.theme.TextColor
import com.shinlee.common.theme.WhiteColor

@Composable
fun TermItem(
    modifier: Modifier,
    titleRes: Int,
    descriptionRes: Int? = null,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = PinkColor,
                uncheckedColor = PinkColor,
                checkmarkColor = WhiteColor,
            )
        )
        Text(
            text = stringResource(titleRes),
            color = TextColor
        )

        if (descriptionRes != null) {
            Text(
                text = stringResource(descriptionRes),
                color = PinkColor
            )
        }
    }
}

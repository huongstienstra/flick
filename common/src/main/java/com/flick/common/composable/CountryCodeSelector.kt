package com.flick.common.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CountryCodeSelector(
    countryCode: String,
    onSelectorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onSelectorClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = countryCode,
                color = Color.DarkGray
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select country code",
                tint = Color.DarkGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountryCodeSelectorPreview() {
    CountryCodeSelector(
        countryCode = "84",
        onSelectorClick = {},
        modifier = Modifier
            .wrapContentSize()
            .height(44.dp)
    )

}
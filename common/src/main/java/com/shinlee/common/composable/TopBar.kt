package com.shinlee.common.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinlee.common.R
import com.shinlee.common.theme.AppSpace
import com.shinlee.common.theme.ShowUpTypography

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    titleRes: Int? = null,
    navigateUp: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(AppSpace.space56dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = navigateUp,
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
        }

        titleRes?.let {
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = stringResource(titleRes),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                style = ShowUpTypography.typography.headlineSmall
            )
        }
    }

}

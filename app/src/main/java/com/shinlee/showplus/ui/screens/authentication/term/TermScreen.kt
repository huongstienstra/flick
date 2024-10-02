package com.shinlee.showplus.ui.screens.authentication.term

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinlee.common.R
import com.shinlee.common.composable.TermItem
import com.shinlee.common.composable.GradientButton
import com.shinlee.common.theme.ShowUpTypography


@Composable
fun TermScreen(
    modifier: Modifier,
    viewModel: TermViewModel,
    onNextClick: () -> Unit
) {
    val uiState by viewModel.uiStateTerm.collectAsStateWithLifecycle()
    val uiCheckBoxState by viewModel.uiCheckBox.collectAsStateWithLifecycle()
    var isActive by remember { mutableStateOf(false) }
    val stringResources = listOf(
        R.string.term_of_service,
        R.string.privacy_policy,
        R.string.marketing_communication
    )


    isActive =
        uiState.isCheckedAll || (uiCheckBoxState[0].isChecked && uiCheckBoxState[1].isChecked)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 100.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.agree_to_term_and_condition),
            style = ShowUpTypography.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(20.dp))

        TermItem(
            modifier = Modifier
                .fillMaxWidth(),
            value = stringResource(
            R.string.select_all), "",
            onTextSelected = {},
            isChecked = uiState.isCheckedAll,
            onCheckedChange = {
                viewModel.checkAllClick()
            }
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
            items(uiCheckBoxState.size) { index ->
                TermItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = stringResource(stringResources[index]),
                    valueClick = " " + stringResource(R.string.require),
                    onTextSelected = {
                        when (stringResources[index]) {
                            R.string.term_of_service -> {}
                            R.string.term_of_service -> {}
                            R.string.marketing_communication -> {}
                            else -> {}
                        }
                    },
                    isChecked = uiCheckBoxState[index].isChecked,
                    onCheckedChange = {
                        viewModel.handleCheckBoxClick(index)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            text = stringResource(R.string.next),
            enable = isActive,
            onClick = {
                onNextClick()
            }
        )
    }


}
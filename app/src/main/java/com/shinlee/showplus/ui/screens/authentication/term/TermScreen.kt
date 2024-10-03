package com.shinlee.showplus.ui.screens.authentication.term

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.shinlee.common.R
import com.shinlee.common.composable.TermItem
import com.shinlee.common.composable.GradientButton
import com.shinlee.common.composable.TopBar
import com.shinlee.common.theme.AppSpace

@Composable
fun TermScreen(
    modifier: Modifier,
    onNextClick: () -> Unit,
    navigateUp: () -> Unit
) {

    val terms = remember {
        listOf(
            Term(1, R.string.term_of_service, R.string.require, true),
            Term(2, R.string.privacy_policy, R.string.require, true),
            Term(3, R.string.marketing_communication, R.string.optional, false),
        )
    }

    var termStates by remember { mutableStateOf(terms.associate { it.id to false }) }
    var selectAllChecked by remember { mutableStateOf(false) }

    val isNextEnabled = remember(termStates) {
        termStates.all { (id, isChecked) ->
            isChecked || !terms.find { it.id == id }!!.isRequired
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSpace.space56dp)
                .background(Color.White),
            titleRes = R.string.sign_up_space,
            navigateUp = {
                navigateUp()
            }
        )

        Spacer(modifier = Modifier.height(AppSpace.space36dp))

        Text(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.agree_to_term_and_condition),
        )

        Spacer(modifier = Modifier.height(AppSpace.space36dp))

        TermItem(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 11.dp, end = 11.dp),
            titleRes = R.string.select_all,
            descriptionRes = null,
            isChecked = selectAllChecked
        ) { isChecked ->
            selectAllChecked = isChecked
            termStates = termStates.mapValues { isChecked }
        }

        Spacer(modifier = Modifier.height(AppSpace.space24dp))

        LazyColumn(
            modifier = Modifier
                .padding(start = 11.dp, end = 11.dp)
        ) {
            items(terms) { term ->
                TermItem(
                    modifier = Modifier.fillMaxWidth(),
                    titleRes = term.title,
                    descriptionRes = term.descriptor,
                    isChecked = termStates[term.id] ?: false
                ) { isChecked ->
                    termStates = termStates.toMutableMap().apply {
                        this[term.id] = isChecked
                    }
                    selectAllChecked = termStates.all { it.value }
                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 32.dp),
            text = stringResource(R.string.next),
            enable = isNextEnabled,
            onClick = {
                onNextClick()
            }
        )
    }
}

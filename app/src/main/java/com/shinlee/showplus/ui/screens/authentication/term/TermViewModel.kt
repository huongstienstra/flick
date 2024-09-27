package com.shinlee.showplus.ui.screens.authentication.term

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TermViewModel : ViewModel() {

    var termUIState = mutableStateOf(TermUIState())


    fun onEvent(event: TermUIEvent) {
        when (event) {
            is TermUIEvent.PrivacyPolicyCheckBoxClicked -> {
                termUIState.value.isSelectAll = true
            }
        }
    }
}
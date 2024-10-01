package com.shinlee.showplus.ui.screens.authentication.term

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class TermUiState(
    var isCheckedAll: Boolean = false,
)

data class ListItem(
    var isChecked: Boolean = false
)

class TermViewModel : ViewModel() {

    private val _uiStateTerm = MutableStateFlow(TermUiState())
    val uiStateTerm: StateFlow<TermUiState> = _uiStateTerm

    private val _uiCheckBox = MutableStateFlow<List<ListItem>>(emptyList())
    val uiCheckBox = _uiCheckBox.asStateFlow()

    init {
        _uiCheckBox.value =
            listOf(
                ListItem(isChecked = false),
                ListItem(isChecked = false),
                ListItem(isChecked = false)
            )
    }

    fun handleCheckBoxClick(index: Int) {
        _uiCheckBox.value = _uiCheckBox.value.mapIndexed { idx, item ->
            if (idx == index) {
                item.copy(isChecked = !item.isChecked)
            } else {
                item
            }
        }
        logicSelectAll()
    }

    fun checkAllClick() {
        _uiCheckBox.value = _uiCheckBox.value.map { item ->
            item.copy(isChecked = !uiStateTerm.value.isCheckedAll)
        }
        _uiStateTerm.update {
            it.copy(
                isCheckedAll = !it.isCheckedAll
            )
        }
    }

    private fun logicSelectAll() {
        if (uiCheckBox.value[0].isChecked
            && uiCheckBox.value[1].isChecked
            && uiCheckBox.value[2].isChecked
        ) {
            _uiStateTerm.update {
                it.copy(isCheckedAll = true)
            }
        } else {
            _uiStateTerm.update {
                it.copy(isCheckedAll = false)
            }
        }
    }

}
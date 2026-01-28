package com.flick.app.ui.state

/**
 * Sealed class representing the different states of UI data loading
 */
sealed class UiState<out T> {
    /**
     * Initial loading state
     */
    data object Loading : UiState<Nothing>()

    /**
     * Success state with data
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * Error state with message
     */
    data class Error(val message: String, val throwable: Throwable? = null) : UiState<Nothing>()

    /**
     * Empty state when data is successfully loaded but contains no items
     */
    data object Empty : UiState<Nothing>()

    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isEmpty: Boolean get() = this is Empty

    /**
     * Returns the data if Success, otherwise null
     */
    fun getOrNull(): T? = (this as? Success)?.data

    /**
     * Returns the error message if Error, otherwise null
     */
    fun errorMessageOrNull(): String? = (this as? Error)?.message
}

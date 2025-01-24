package com.albertorusso.randomuser.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertorusso.randomuser.di.UseCases
import com.albertorusso.randomuser.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val useCases: UseCases
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> get() = _errorState

    fun getUser(email: String) {
        _loadingState.value = true
        viewModelScope.launch {
            try {
                val fetchedUser = useCases.getUserByEmail.execute(email)
                _user.value = fetchedUser
            } catch (e: Exception) {
                _errorState.value = "Failed to load user: ${e.message}"
            } finally {
                _loadingState.value = false
            }
        }
    }
}
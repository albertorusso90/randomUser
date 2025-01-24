package com.albertorusso.randomuser.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertorusso.randomuser.di.UseCases
import com.albertorusso.randomuser.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserListViewModel(
    private val useCases: UseCases
) : ViewModel() {

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> get() = _userList

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> get() = _errorState

    init {
        getUsers(false)
    }

    fun getUsers(forceRefresh: Boolean = false) {
        _loadingState.value = true
        viewModelScope.launch {
            try {
                val users = useCases.getUsers.execute(forceRefresh)
                _userList.value = users
            } catch (e: Exception) {
                _errorState.value = "Failed to load users: ${e.message}"
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun searchUser(searchTerm: String) {
        _loadingState.value = true
        viewModelScope.launch {
            try {
                val users = useCases.searchUsers.execute(searchTerm = searchTerm)
                _userList.value = users
            } catch (e: Exception) {
                _errorState.value = "Failed to find users: ${e.message}"
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun deleteUser(email: String) {
        viewModelScope.launch {
            try {
                useCases.hideUser.execute(email = email)
                _userList.value = _userList.value.filterNot { it.email == email }
            } catch (e: Exception) {
                _errorState.value = "Failed to delete user: ${e.message}"
            }
        }
    }
}
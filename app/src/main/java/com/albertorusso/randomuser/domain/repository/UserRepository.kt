package com.albertorusso.randomuser.domain.repository

import com.albertorusso.randomuser.domain.model.User

interface UserRepository {
    suspend fun getUsers(forceRefresh: Boolean = false): List<User>
    suspend fun getUser(email: String): User
    suspend fun searchUsers(searchTerm: String): List<User>
    suspend fun hideUser(email: String)
}
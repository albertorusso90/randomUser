package com.albertorusso.randomuser.domain.usecase

import com.albertorusso.randomuser.domain.model.User
import com.albertorusso.randomuser.domain.repository.UserRepository

class SearchUsers(private val userRepository: UserRepository) {
    suspend fun execute(searchTerm: String): List<User> {
        return userRepository.searchUsers(searchTerm)
    }
}
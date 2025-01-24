package com.albertorusso.randomuser.domain.usecase

import com.albertorusso.randomuser.domain.model.User
import com.albertorusso.randomuser.domain.repository.UserRepository

class GetUsers(private val userRepository: UserRepository) {
    suspend fun execute(forceRefresh: Boolean): List<User> {
        return userRepository.getUsers(forceRefresh)
    }
}
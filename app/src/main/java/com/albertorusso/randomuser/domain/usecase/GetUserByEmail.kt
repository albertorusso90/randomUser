package com.albertorusso.randomuser.domain.usecase

import com.albertorusso.randomuser.domain.model.User
import com.albertorusso.randomuser.domain.repository.UserRepository

class GetUserByEmail(private val userRepository: UserRepository) {
    suspend fun execute(email: String): User {
        return userRepository.getUser(email)
    }
}
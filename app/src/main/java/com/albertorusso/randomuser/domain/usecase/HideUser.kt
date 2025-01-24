package com.albertorusso.randomuser.domain.usecase

import com.albertorusso.randomuser.domain.repository.UserRepository

class HideUser(private val userRepository: UserRepository) {
    suspend fun execute(email: String) {
        userRepository.hideUser(email)
    }
}
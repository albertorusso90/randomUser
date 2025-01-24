package com.albertorusso.randomuser.domain.usecase

import com.albertorusso.randomuser.domain.repository.UserRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class HideUserTest {

    private var userRepository: UserRepository = mockk(relaxed = true)
    private lateinit var hideUser: HideUser

    @Before
    fun setUp() {
        hideUser = HideUser(userRepository)
    }

    @Test
    fun `execute should call userRepository hideUser with correct email`() = runTest {
        // Given
        val email = "test@example.com"

        // When
        hideUser.execute(email)

        // Then
        coVerify { userRepository.hideUser(email) } // Verify the repository method is called with the correct email
    }
}
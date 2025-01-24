package com.albertorusso.randomuser.domain.usecase

import com.albertorusso.randomuser.domain.model.User
import com.albertorusso.randomuser.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetUserByEmailTest {

    private var userRepository: UserRepository = mockk(relaxed = true)
    private lateinit var getUserByEmail: GetUserByEmail

    @Before
    fun setUp() {

        getUserByEmail = GetUserByEmail(userRepository)
    }

    @Test
    fun `execute should call userRepository getUser with correct email`() = runTest {
        // Given
        val email = "test@example.com"
        val user = User(
            email = email,
            gender = "male",
            firstName = "John",
            lastName = "Doe",
            phone = "1234567890",
            locationStreet = "123 Main St",
            locationCity = "City",
            locationState = "State",
            registeredDate = "2021-01-01",
            pictureSmall = "urlSmall",
            pictureMedium = "urlMedium",
            pictureBig = "urlBig"
        )

        coEvery { userRepository.getUser(email) } returns user

        val result = getUserByEmail.execute(email)

        coVerify { userRepository.getUser(email) }
        assertEquals(user, result)
    }

    @Test
    fun `execute should throw exception if userRepository throws an exception`() = runTest {
        // Given
        val email = "test@example.com"

        // Mock the repository's method to throw an exception
        coEvery { userRepository.getUser(email) } throws Exception("User not found")

        // When & Then
        try {
            getUserByEmail.execute(email)
            // Fail the test if no exception is thrown
            assert(false)
        } catch (e: Exception) {
            // Verify the exception message
            assertEquals("User not found", e.message)
        }

        coVerify { userRepository.getUser(email) }
    }
}
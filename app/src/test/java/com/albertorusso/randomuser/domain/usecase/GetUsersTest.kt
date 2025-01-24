package com.albertorusso.randomuser.domain.usecase

import com.albertorusso.randomuser.domain.model.User
import com.albertorusso.randomuser.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class GetUsersTest {
    private var userRepository: UserRepository = mockk(relaxed = true)
    private lateinit var getUsers: GetUsers

    @Before
    fun setUp() {
        getUsers = GetUsers(userRepository)
    }

    @Test
    fun `execute should call userRepository getUsers with correct argument and return users list`() = runTest {
        // Given
        val forceRefresh = true
        val users = listOf(
            User("user1@example.com", "male", "John", "Doe", "123456789", "Street 1", "City", "State", "2020-01-01", "urlSmall", "urlMedium", "urlBig"),
            User("user2@example.com", "female", "Jane", "Doe", "987654321", "Street 2", "City", "State", "2021-01-01", "urlSmall", "urlMedium", "urlBig")
        )

        // Mock the repository method to return a list of users
        coEvery { userRepository.getUsers(forceRefresh) } returns users

        // When
        val result = getUsers.execute(forceRefresh)

        // Then
        assertEquals(users, result)  // Verify that the returned list is correct
        coVerify { userRepository.getUsers(forceRefresh) }  // Verify that getUsers was called with the correct argument
    }

    @Test
    fun `execute should return an empty list when userRepository returns an empty list`() = runTest {
        // Given
        val forceRefresh = true
        val emptyList = emptyList<User>()

        // Mock the repository method to return an empty list
        coEvery { userRepository.getUsers(forceRefresh) } returns emptyList

        // When
        val result = getUsers.execute(forceRefresh)

        // Then
        assertEquals(emptyList, result)  // Verify that the result is an empty list
        assertTrue(result.isEmpty())
        coVerify { userRepository.getUsers(forceRefresh) }  // Verify that getUsers was called with the correct argument
    }
}
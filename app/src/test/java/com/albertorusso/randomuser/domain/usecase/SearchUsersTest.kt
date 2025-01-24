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

class SearchUsersTest {
    private var userRepository: UserRepository = mockk(relaxed = true)
    private lateinit var searchUsers: SearchUsers

    @Before
    fun setUp() {
        searchUsers = SearchUsers(userRepository)
    }

    @Test
    fun `execute should call userRepository searchUsers with correct searchTerm and return users`() = runTest {
        // Given
        val searchTerm = "John"
        val userList = listOf(
            User(
                email = "john@example.com",
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
        )

        // Mocking the repository method
        coEvery { userRepository.searchUsers(searchTerm) } returns userList

        // When
        val result = searchUsers.execute(searchTerm)

        // Then
        assertEquals(userList, result) // Verify the returned list is the same as the mocked list
        coVerify { userRepository.searchUsers(searchTerm) } // Verify the repository method was called with the correct searchTerm
    }
}
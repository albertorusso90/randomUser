package com.albertorusso.randomuser.presentation.list


import com.albertorusso.randomuser.di.UseCases
import com.albertorusso.randomuser.domain.model.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var userListViewModel: UserListViewModel
    private var useCases: UseCases = mockk()
    private val users = listOf(
        User(
            email = "test1@example.com",
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
        ),
        User(
            email = "test2@example.com",
            gender = "female",
            firstName = "Jane",
            lastName = "Doe",
            phone = "0987654321",
            locationStreet = "456 Elm St",
            locationCity = "Town",
            locationState = "Region",
            registeredDate = "2020-01-01",
            pictureSmall = "urlSmall",
            pictureMedium = "urlMedium",
            pictureBig = "urlBig"
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        userListViewModel = UserListViewModel(useCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `getUsers should update userList and loadingState on success`() = runTest {
        coEvery { useCases.getUsers.execute(any()) } returns users

        userListViewModel.getUsers()

        assertTrue(userListViewModel.loadingState.value)

        advanceUntilIdle()

        assertEquals(users, userListViewModel.userList.value)
        assertFalse(userListViewModel.loadingState.value)
        coVerify { useCases.getUsers.execute(any()) }
    }

    @Test
    fun `getUsers should update errorState on failure`() = runTest {
        val exceptionMessage = "Failed to load users"
        coEvery { useCases.getUsers.execute(any()) } throws Exception(exceptionMessage)

        userListViewModel.getUsers()

        assertTrue(userListViewModel.loadingState.value)

        advanceUntilIdle()

        assertEquals("Failed to load users: $exceptionMessage", userListViewModel.errorState.value)
        assertFalse(userListViewModel.loadingState.value)
        coVerify { useCases.getUsers.execute(any()) }
    }

    @Test
    fun `searchUser should update userList and loadingState on success`() = runTest {
        val searchTerm = "John"
        coEvery { useCases.searchUsers.execute(searchTerm) } returns listOf(users[0])

        userListViewModel.searchUser(searchTerm)

        assertTrue(userListViewModel.loadingState.value)

        advanceUntilIdle()

        assertEquals(listOf(users[0]), userListViewModel.userList.value)
        assertFalse(userListViewModel.loadingState.value)
        coVerify { useCases.searchUsers.execute(searchTerm) }
    }

    @Test
    fun `searchUser should send emoty list and loadingState on success`() = runTest {
        val searchTerm = ""
        coEvery { useCases.searchUsers.execute(searchTerm) } returns emptyList()

        userListViewModel.searchUser(searchTerm)

        assertTrue(userListViewModel.loadingState.value)

        advanceUntilIdle()

        assertTrue(userListViewModel.userList.value.isEmpty())
        assertFalse(userListViewModel.loadingState.value)
        coVerify { useCases.searchUsers.execute(searchTerm) }
    }

    @Test
    fun `searchUser should update errorState on failure`() = runTest {
        val exceptionMessage = "Failed to search users"
        coEvery { useCases.searchUsers.execute(any()) } throws Exception(exceptionMessage)

        userListViewModel.searchUser("John")

        assertTrue(userListViewModel.loadingState.value)

        advanceUntilIdle()

        assertEquals("Failed to find users: $exceptionMessage", userListViewModel.errorState.value)
        assertFalse(userListViewModel.loadingState.value)
        coVerify { useCases.searchUsers.execute(any()) }
    }

    @Test
    fun `deleteUser should update errorState on failure`() = runTest {
        val email = "test1@example.com"
        val exceptionMessage = "Failed to delete user"
        coEvery { useCases.hideUser.execute(email) } throws Exception(exceptionMessage)

        userListViewModel.deleteUser(email)

        advanceUntilIdle()

        assertEquals("Failed to delete user: $exceptionMessage", userListViewModel.errorState.value)
        coVerify { useCases.hideUser.execute(email) }
    }
}
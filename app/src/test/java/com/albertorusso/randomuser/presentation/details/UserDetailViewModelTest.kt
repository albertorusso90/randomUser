package com.albertorusso.randomuser.presentation.details

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
class UserDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var userDetailViewModel: UserDetailViewModel
    private var useCases: UseCases = mockk()
    private val user = User(
        email = "test@example.com",
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

    @Before
    fun setUp() {
        // Set the Main dispatcher to the StandardTestDispatcher
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        userDetailViewModel = UserDetailViewModel(useCases)
    }

    @After
    fun tearDown() {
        // Reset the Main dispatcher after the test is complete
        Dispatchers.resetMain()
        testDispatcher.cancel() // Clean up any pending coroutines
    }

    @Test
    fun `getUser should update user and loading state on success`() = runTest {
        val email = "test@example.com"
        coEvery { useCases.getUserByEmail.execute(email) } returns user

        userDetailViewModel.getUser(email)

        assertTrue(userDetailViewModel.loadingState.value) // loading state should be true before fetching
        // Allow the coroutine to finish
        advanceUntilIdle()

        assertEquals(user, userDetailViewModel.user.value) // user should be updated with the fetched user
        assertFalse(userDetailViewModel.loadingState.value) // loading state should be false after fetching
        coVerify { useCases.getUserByEmail.execute(email) } // ensure that getUserByEmail.execute is called
    }

    @Test
    fun `getUser should update error state on failure`() = runTest {
        val email = "test@example.com"
        val exceptionMessage = "User not found"
        coEvery { useCases.getUserByEmail.execute(email) } throws Exception(exceptionMessage)

        userDetailViewModel.getUser(email)

        assertTrue(userDetailViewModel.loadingState.value) // loading state should be true before fetching

        // Allow the coroutine to finish
        advanceUntilIdle()

        assertEquals("Failed to load user: $exceptionMessage", userDetailViewModel.errorState.value) // error state should contain the exception message
        assertTrue(userDetailViewModel.loadingState.value == false) // loading state should be false after fetching
        coVerify { useCases.getUserByEmail.execute(email) } // ensure that getUserByEmail.execute is called
    }
}
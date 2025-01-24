package com.albertorusso.randomuser.di

import androidx.room.Room
import com.albertorusso.randomuser.data.datasource.local.UserDatabase
import com.albertorusso.randomuser.data.datasource.remote.UserApiService
import com.albertorusso.randomuser.data.datasource.remote.UserRemoteSource
import com.albertorusso.randomuser.data.repository.UserRepositoryImpl
import com.albertorusso.randomuser.domain.repository.UserRepository
import com.albertorusso.randomuser.domain.usecase.GetUserByEmail
import com.albertorusso.randomuser.domain.usecase.GetUsers
import com.albertorusso.randomuser.domain.usecase.HideUser
import com.albertorusso.randomuser.domain.usecase.SearchUsers
import com.albertorusso.randomuser.presentation.details.UserDetailViewModel
import com.albertorusso.randomuser.presentation.list.UserListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Koin module for dependency injection
val appModule = module {
// Provide Retrofit instance for making network calls
    single { provideRetrofit() }

    // Provide UserApiService (Retrofit service)
    single { get<Retrofit>().create(UserApiService::class.java) }

    // Provide UserRemoteSource (needs Retrofit)
    single { UserRemoteSource(get()) }

    // Provide UserDatabase as a singleton
    single {
        Room.databaseBuilder(get(), UserDatabase::class.java, "user_database")
            .fallbackToDestructiveMigration()  // Handle migrations if needed
            .build()
    }

    // Provide UserDao from the UserDatabase instance
    single { get<UserDatabase>().userDao() }

    // Provide UserRepository (injected with UserRemoteSource and UserDao)
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    // Use `factory` for stateless use cases to ensure fresh instances
    factory { GetUsers(get()) }
    factory { GetUserByEmail(get()) }
    factory { SearchUsers(get()) }
    factory { HideUser(get()) }

    // Provide UseCases wrapper
    single { UseCases(get(), get(), get(), get()) }

    // Provide UserListViewModel with UseCases
    viewModel { UserListViewModel(get()) }

    // Provide UserDetailViewModel with UseCases
    viewModel { UserDetailViewModel(get()) }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.randomuser.me")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

// Wrapper class to group use cases
class UseCases(
    val getUsers: GetUsers,
    val getUserByEmail: GetUserByEmail,
    val searchUsers: SearchUsers,
    val hideUser: HideUser
)
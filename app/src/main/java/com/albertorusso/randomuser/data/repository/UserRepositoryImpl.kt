package com.albertorusso.randomuser.data.repository

import com.albertorusso.randomuser.data.datasource.local.UserDao
import com.albertorusso.randomuser.data.datasource.remote.UserRemoteSource
import com.albertorusso.randomuser.data.mapper.toDomain
import com.albertorusso.randomuser.data.mapper.toLocal
import com.albertorusso.randomuser.domain.model.User
import com.albertorusso.randomuser.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userRemoteSource: UserRemoteSource,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUsers(forceRefresh: Boolean): List<User> {
        var localUsers = userDao.getAllUsers()

        if (localUsers.isEmpty() || forceRefresh) {
            val remoteUsers = userRemoteSource.getUsers()

            remoteUsers.forEach { userResult ->
                val userLocal = userResult.toLocal()
                userDao.insertUser(userLocal)
            }
        }

        localUsers = userDao.getAllUsers()
        return localUsers.map { it.toDomain() }
    }

    override suspend fun getUser(email: String): User {
        return userDao.getUserByEmail(email).toDomain()
    }

    override suspend fun searchUsers(searchTerm: String): List<User> {
        val users = userDao.searchUsers("%$searchTerm%")
        return users.map { it.toDomain() }
    }

    override suspend fun hideUser(email: String) {
        userDao.markUserAsDeleted(email)
    }
}
package com.albertorusso.randomuser.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.albertorusso.randomuser.data.model.UserLocal

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserLocal)

    @Query("SELECT * FROM users WHERE deleted = 0")
    suspend fun getAllUsers(): List<UserLocal>

    @Query("SELECT * FROM users WHERE email = :email AND deleted = 0 LIMIT 1")
    suspend fun getUserByEmail(email: String): UserLocal

    @Query("SELECT * FROM users WHERE email LIKE :searchTerm OR firstName LIKE :searchTerm OR lastName LIKE :searchTerm AND deleted = 0")
    suspend fun searchUsers(searchTerm: String): List<UserLocal>

    @Query("UPDATE users SET deleted = 1 WHERE email = :email")
    suspend fun markUserAsDeleted(email: String)

    @Delete
    suspend fun deleteUser(user: UserLocal)
}
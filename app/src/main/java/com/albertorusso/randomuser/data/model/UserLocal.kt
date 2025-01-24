package com.albertorusso.randomuser.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserLocal(
    @PrimaryKey val email: String,
    val firstName: String,
    val lastName: String,
    val pictureBig: String,
    val pictureMedium: String,
    val pictureSmall: String,
    val phone: String,
    val gender: String,
    val street: String,
    val city: String,
    val state: String,
    val registeredDate: String,
    val deleted: Boolean = false
)
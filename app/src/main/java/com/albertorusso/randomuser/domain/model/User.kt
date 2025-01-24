package com.albertorusso.randomuser.domain.model

data class User(
    val email: String,
    val gender: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val locationStreet: String,
    val locationCity: String,
    val locationState: String,
    val registeredDate: String,
    val pictureSmall: String,
    val pictureMedium: String,
    val pictureBig: String
)
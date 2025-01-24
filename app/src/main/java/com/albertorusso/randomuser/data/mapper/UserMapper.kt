package com.albertorusso.randomuser.data.mapper

import com.albertorusso.randomuser.data.model.UserLocal
import com.albertorusso.randomuser.data.model.UserResult
import com.albertorusso.randomuser.domain.model.User

fun UserResult.toLocal(): UserLocal {
    return UserLocal(
        email = this.email,
        firstName = this.name.first,
        lastName = this.name.last,
        pictureBig = this.picture.large,
        pictureMedium = this.picture.medium,
        pictureSmall = this.picture.thumbnail,
        phone = this.phone,
        gender = this.gender,
        street = this.location.street.name,
        city = this.location.city,
        state = this.location.state,
        registeredDate = this.registered.date,
        deleted = false
    )
}

fun UserLocal.toDomain(): User {
    return User(
        email = this.email,
        gender = this.gender,
        firstName = this.firstName,
        lastName = this.lastName,
        phone = this.phone,
        locationStreet = this.street,
        locationCity = this.city,
        locationState = this.state,
        registeredDate = this.registeredDate,
        pictureSmall = this.pictureSmall,
        pictureMedium = this.pictureMedium,
        pictureBig = this.pictureBig
    )
}
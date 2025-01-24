package com.albertorusso.randomuser.data.datasource.remote

import com.albertorusso.randomuser.data.model.UserRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {
    @GET("/")
    suspend fun getUsers(
        @Query("results") results: Int
    ): Response<UserRemote>
}
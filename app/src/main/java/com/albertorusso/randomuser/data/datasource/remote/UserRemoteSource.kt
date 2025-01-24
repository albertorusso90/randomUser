package com.albertorusso.randomuser.data.datasource.remote

import android.util.Log
import com.albertorusso.randomuser.data.model.UserResult

class UserRemoteSource(private val apiService: UserApiService) {

    suspend fun getUsers(results: Int = 20): List<UserResult> {
        val response = apiService.getUsers(results)
        Log.d("API_CALL", "Request URL: ${response.raw().request.url}")
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.results
        }

        return emptyList()
    }
}
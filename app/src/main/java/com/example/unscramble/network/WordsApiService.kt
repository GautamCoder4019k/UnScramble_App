package com.example.unscramble.network

import retrofit2.http.GET

interface WordsApiService {

    @GET("api?words=10")
    suspend fun getWords(): List<String>

}
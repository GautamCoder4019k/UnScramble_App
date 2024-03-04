package com.example.unscramble.data

import com.example.unscramble.network.WordsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val wordsRepository: WordsRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://random-word-api.vercel.app/"

    private val retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()

    private val retrofitService: WordsApiService by lazy {
        retrofit.create(WordsApiService::class.java)
    }

    override val wordsRepository: WordsRepository by lazy {
        NetworkWordsRepository(retrofitService)
    }
}
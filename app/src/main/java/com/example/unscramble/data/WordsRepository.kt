package com.example.unscramble.data

import com.example.unscramble.network.WordsApiService

interface WordsRepository {
    suspend fun getWords(): List<String>
}

class NetworkWordsRepository(private val wordsApiService: WordsApiService) : WordsRepository {
    override suspend fun getWords(): List<String> = wordsApiService.getWords()
}
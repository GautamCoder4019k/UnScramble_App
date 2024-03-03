package com.example.unscramble.ui

data class GameUiState(
    val currScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val currWordCount: Int = 1,
    val currHintCount:Int=1,
    val score: Int = 0,
    val isGameOver:Boolean=false,
)

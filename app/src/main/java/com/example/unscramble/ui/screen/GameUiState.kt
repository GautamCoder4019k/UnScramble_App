package com.example.unscramble.ui.screen

import com.example.unscramble.data.MAX_NO_OF_HINTS

data class GameUiState(
    val currScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val currWordCount: Int = 1,
    val currHintCount:Int= MAX_NO_OF_HINTS,
    val score: Int = 0,
    val isGameOver:Boolean=false,
    val showHint:Boolean=false,
    val isHintTaken:Boolean=false
)

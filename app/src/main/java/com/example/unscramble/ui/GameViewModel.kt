package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_DECREASE
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {


    //Game UI State
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var firstWord = ""
        private set
    var lastWord = ""
        private set

    var skippedWords = mutableListOf<Pair<String, String>>()
        private set

    var userGuess by mutableStateOf("")
        private set

    private lateinit var currWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()

    init {
        resetGame()
    }

    fun dismissHint() {
        _uiState.update {
            it.copy(showHint = false)
        }
    }

    fun resetGame() {
        usedWords.clear()
        skippedWords.clear()
        _uiState.value = GameUiState(currScrambledWord = pickRandomWordAndShuffle())
    }

    fun skipWord() {
        skippedWords.add(Pair(currWord, _uiState.value.currScrambledWord))
        updateGameState(uiState.value.score)
        updateUserGuess("")
    }

    fun hintAction() {
        if (_uiState.value.isHintTaken) {
            _uiState.update {
                it.copy(showHint = true)
            }
        } else {
            if (_uiState.value.currHintCount > 0) {
                firstWord = currWord[0].toString()
                lastWord = currWord[currWord.length - 1].toString()
                _uiState.update {
                    it.copy(
                        currHintCount = it.currHintCount.dec(),
                        showHint = true,
                        isHintTaken = true,
                        score = it.score.plus(SCORE_DECREASE)
                    )
                }
            }
        }
    }

    fun updateUserGuess(guessWord: String) {
        userGuess = guessWord
    }

    fun checkUserGuess() {
        if (userGuess.equals(currWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            _uiState.update {
                it.copy(isGuessedWordWrong = true, score = it.score.dec())
            }
        }
        updateUserGuess("")
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.update {
                it.copy(
                    isGuessedWordWrong = false, score = updatedScore, isGameOver = true
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isGuessedWordWrong = false,
                    currScrambledWord = pickRandomWordAndShuffle(),
                    score = updatedScore,
                    currWordCount = it.currWordCount.inc(),
                    isHintTaken = false
                )
            }
        }

    }

    private fun pickRandomWordAndShuffle(): String {
        currWord = allWords.random()
        return if (usedWords.contains(currWord)) pickRandomWordAndShuffle()
        else {
            usedWords.add(currWord)
            shuffleCurrentWord(currWord)
        }
    }


    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()

        while (String(tempWord) == word) tempWord.shuffle()

        return String(tempWord)
    }


}

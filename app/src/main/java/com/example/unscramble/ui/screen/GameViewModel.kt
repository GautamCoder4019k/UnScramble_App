package com.example.unscramble.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unscramble.UnScrambleApplication
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_DECREASE
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.WordsRepository
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class GameViewModel(private val wordsRepository: WordsRepository) : ViewModel() {


    //Game UI State
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var words = listOf<String>()

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
        words= listOf()
        getWords()
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
        if (userGuess.trim().equals(currWord, ignoreCase = true)) {
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
        currWord = if (words.isEmpty()) allWords.random() else words.random()
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

    private fun getWords() {
        viewModelScope.launch {
            words = try {
                wordsRepository.getWords()
            } catch (e: IOException) {
                listOf()
            }
            println(words)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as UnScrambleApplication)
                val wordsRepository = application.container.wordsRepository
                GameViewModel(wordsRepository = wordsRepository)
            }
        }
    }


}

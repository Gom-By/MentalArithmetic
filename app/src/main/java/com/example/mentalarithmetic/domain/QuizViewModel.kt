package com.example.mentalarithmetic.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mentalarithmetic.data.PlayerDao
import com.example.mentalarithmetic.data.PlayerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

class QuizViewModel(val playerDao: PlayerDao): ViewModel() {
    private val _players = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val players: StateFlow<List<PlayerEntity>> = _players.asStateFlow()

    private val _topPlayers = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val topPlayers: StateFlow<List<PlayerEntity>> = _topPlayers.asStateFlow()

    private val _correctAnswer = mutableIntStateOf(0)
    val correctAnswer: State<Int> = _correctAnswer.asIntState()

    private val _isGameTerminated = mutableStateOf(false)
    val isGameTerminated: State<Boolean> = _isGameTerminated

    private val _question = mutableStateOf("")
    val question: State<String> = _question

    private val _score = mutableIntStateOf(0)
    val score: State<Int> = _score.asIntState()

    private val _lives = mutableIntStateOf(3)
    val lives: State<Int> = _lives.asIntState()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playerDao.getPlayers().collect { lstPlayers ->
                _players.value = lstPlayers
            }
            playerDao.getTopPlayers().collect { lstPlayers ->
                _topPlayers.value = lstPlayers
            }
        }
    }

    fun firstLaunch() {
        _isGameTerminated.value = false
        _score.intValue = 0
        _lives.intValue = 3
        generateQuestion()
    }

    fun generateQuestion() {
        val a = Random.nextInt(0, 100)
        val b = Random.nextInt(0, 100)
        val symbol = Operation.entries.random()
        val result = symbol.calc(a, b)

        _correctAnswer.intValue = result
        _question.value = "$a ${symbol.opSymbol} $b = "
    }

    fun confirmAnswer(answer: String): Boolean? {
        try {
            if(answer.toInt() == _correctAnswer.intValue) {
                _score.intValue += 1
                return true
            } else {
                // _score.intValue = max(0, _score.intValue - 1)
                _lives.intValue = max(0, _lives.intValue - 1)
                if(_lives.intValue <= 0) {
                    _isGameTerminated.value = true
                }
                return false
            }
        } catch (e: Exception) {
            // TODO: Toast here
            return null
        }
    }

    fun passQuestion() {
        _lives.intValue = max(0, _lives.intValue - 1)
        generateQuestion()
    }

    fun useHelp() {
        _score.intValue = max(0, _score.intValue - 3)
    }

    fun registerPlayer(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playerDao.insertPlayer(
                PlayerEntity(
                    name = name,
                    score = _score.intValue,
                    lives = _lives.intValue
            ))
        }
    }

    fun deleteRecords(lstPlayers: List<PlayerEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            playerDao.deletePlayers(lstPlayers)
        }
    }
}


enum class Operation(internal val opSymbol: String) {
    ADD("+") {
        override fun calc(n1: Int, n2: Int): Int {
            return n1 + n2
        }
    },
    SUBTRACT("-") {
        override fun calc(n1: Int, n2: Int): Int {
            return n1 - n2
        }
    },
    MULTIPLY("*") {
        override fun calc(n1: Int, n2: Int): Int {
            return n1 * n2
        }
    },
    DIVIDE("/") {
        override fun calc(n1: Int, n2: Int): Int {
            return (n1 / n2).toInt()
        }
    };

    abstract fun calc(n1: Int, n2: Int): Int
}

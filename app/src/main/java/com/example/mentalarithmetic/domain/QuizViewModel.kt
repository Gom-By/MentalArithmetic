package com.example.mentalarithmetic.domain

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.asLongState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mentalarithmetic.data.PlayerDao
import com.example.mentalarithmetic.data.PlayerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.Locale
import kotlin.math.max
import kotlin.random.Random

class QuizViewModel(val playerDao: PlayerDao, ctx: Context) : ViewModel() {
    private val _language: MutableState<String> = mutableStateOf("en")
    val language: State<String> = _language

    @SuppressLint("StaticFieldLeak")
    private val context = ctx.applicationContext

    init {
        viewModelScope.launch {
            LanguagePreferenceManager.getLanguageFlow(context)
                .collect { lang ->
                    _language.value = lang
                }
        }
    }

    fun updateLanguage(lang: String) {
        viewModelScope.launch {
            LanguagePreferenceManager.saveLanguage(context, lang)
        }
        _language.value = lang
    }

    fun applyLocale(context: Context): Context {
        return LocaleHelper.setLocale(context, _language.value)
    }


    private val _players = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val players: StateFlow<List<PlayerEntity>> = _players.asStateFlow()

    private val _topPlayers = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val topPlayers: StateFlow<List<PlayerEntity>> = _topPlayers.asStateFlow()

    private val _correctAnswer = mutableLongStateOf(0)
    val correctAnswer: State<Long> = _correctAnswer.asLongState()

    private val _isGameTerminated = mutableStateOf(false)
    val isGameTerminated: State<Boolean> = _isGameTerminated

    private val _question = mutableStateOf("")
    val question: State<String> = _question

    private val _score = mutableIntStateOf(0)
    val score: State<Int> = _score.asIntState()

    private val _lives = mutableIntStateOf(3)
    val lives: State<Int> = _lives.asIntState()

    var gameDifficulty = mutableStateOf(Difficulty.EASY)

    var timerText = mutableStateOf("")
    val timer = object : CountDownTimer(20000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timerText.value = "${millisUntilFinished / 1000} seconds remaining"
        }

        override fun onFinish() {
            timerText.value = "Time's up !"
            _isGameTerminated.value = true
        }
    }

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

    fun firstLaunch(difficulty: Difficulty = Difficulty.EASY) {
        gameDifficulty.value = difficulty
        _isGameTerminated.value = false
        _score.intValue = 0
        _lives.intValue = when (difficulty) {
            Difficulty.EASY -> 10
            Difficulty.MEDIUM -> 6
            Difficulty.HARD -> 5
            Difficulty.EXPERT -> 3
        }
        generateQuestion(difficulty)
    }

    fun generateQuestion(difficulty: Difficulty = Difficulty.EASY) {
        timer.cancel()
        timer.start()
        val limits = when (difficulty) {
            Difficulty.EASY -> 0 to 10
            Difficulty.MEDIUM -> 0 to 20
            Difficulty.HARD -> -20 to 20
            Difficulty.EXPERT -> -100 to 100
        }

        val a = Random.nextInt(limits.first, limits.second)
        val b = Random.nextInt(limits.first, limits.second)
        val symbol = Operation.entries.random()
        val result: Long = symbol.calc(a, b)

        _correctAnswer.longValue = result.toBigDecimal().setScale(2, RoundingMode.UP).toLong()
        _question.value = "$a ${symbol.opSymbol} $b = "
    }

    fun confirmAnswer(answer: String): Boolean? {
        try {
            if (answer.toLong() == _correctAnswer.longValue) {
                _score.intValue += 1
                return true
            } else {
                // _score.intValue = max(0, _score.intValue - 1)
                _lives.intValue = max(0, _lives.intValue - 1)
                if (_lives.intValue <= 0) {
                    _isGameTerminated.value = true
                }
                return false
            }
        } catch (_: Exception) {
            return null
        }
    }

    fun passQuestion() {
        _lives.intValue = max(0, _lives.intValue - 1)
        if (_lives.intValue == 0) {
            _isGameTerminated.value = true
            return
        }
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
                    lives = _lives.intValue,
                    gameMode = gameDifficulty.value
                )
            )
        }
    }

    fun deleteRecords(lstPlayers: List<PlayerEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            playerDao.deletePlayers(lstPlayers)
        }
    }
}

object LocaleHelper {
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}

enum class Operation(internal val opSymbol: String) {
    ADD("+") {
        override fun calc(n1: Int, n2: Int): Long {
            return (n1 + n2).toLong()
        }
    },
    SUBTRACT("-") {
        override fun calc(n1: Int, n2: Int): Long {
            return (n1 - n2).toLong()
        }
    },
    MULTIPLY("*") {
        override fun calc(n1: Int, n2: Int): Long {
            return (n1 * n2).toLong()
        }
    },
    DIVIDE("/") {
        override fun calc(n1: Int, n2: Int): Long {
            return (n1 / n2).toLong()
        }
    };

    abstract fun calc(n1: Int, n2: Int): Long
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
    EXPERT
}
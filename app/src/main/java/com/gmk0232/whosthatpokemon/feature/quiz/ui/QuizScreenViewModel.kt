package com.gmk0232.whosthatpokemon.feature.quiz.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmk0232.whosthatpokemon.feature.quiz.data.repository.FetchPokemonException
import com.gmk0232.whosthatpokemon.feature.quiz.domain.DetermineCorrectPokemonSelectedUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetScoreUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState.Correct
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState.Incorrect
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState.Unanswered
import com.gmk0232.whosthatpokemon.feature.quiz.domain.SetScoreUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.ui.QuizRoundState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val RESULT_DISPLAY_TIMEOUT = 1500L

@HiltViewModel
class QuizScreenViewModel @Inject constructor(
    private val getPokemonQuizRoundDataUseCase: GetPokemonQuizRoundDataUseCase,
    private val determineCorrectPokemonSelectedUseCase: DetermineCorrectPokemonSelectedUseCase,
    private val getScoreUseCase: GetScoreUseCase
) : ViewModel() {

    /*
    If we want to keep the score on screen independent of other data like the round data
    We could model it as it's own separate state object which can have a loading state too
    For now we'll initialise with a default value of -1 to signal to the UI it's not loaded yet
     */
    private val _quizScreenUIState: MutableStateFlow<QuizScreenUIState> = MutableStateFlow(
        QuizScreenUIState(quizRoundState = Loading, -1)
    )

    val quizScreenUIState = _quizScreenUIState.asStateFlow()

    init {
        loadQuizRoundData()
    }

    fun loadQuizRoundData() {
        viewModelScope.launch(Dispatchers.Main) {

            _quizScreenUIState.update { currentState ->
                currentState.copy(quizRoundState = Loading)
            }

            val newQuizRoundDataResult = withContext(Dispatchers.IO) {
                runCatching {
                    val quizRoundState = getPokemonQuizRoundDataUseCase.execute()
                    val currentScore = getScoreUseCase.execute()
                    _quizScreenUIState.value.copy(
                        quizRoundState = QuizRoundDataReady(
                            quizRoundState,
                            Unanswered
                        ), score = currentScore
                    )
                }
            }

            newQuizRoundDataResult.fold(
                onSuccess = { state ->
                    _quizScreenUIState.emit(state)
                },
                onFailure = { error ->
                    onGetNewRoundDataError(error)
                }
            )

        }
    }

    fun onImageLoadError() {
        Log.e("Error:", "Coil could not load the image")
        _quizScreenUIState.update {
            it.copy(quizRoundState = Error("Could not load pokemon image!"))
        }
    }

    private fun onGetNewRoundDataError(it: Throwable) {
        /*
        If an error occurs, check if it was was FetchPokemonException, indicating we got a server response at least and log it
        Other wise log a generic error message
        */
        Log.e("Error:", it.message ?: "Something went wrong $it")
        val errorMessage = if (it is FetchPokemonException) {
            it.message
        } else {
            "Something went wrong, please try again"
        }

        _quizScreenUIState.update { currentState ->
            currentState.copy(quizRoundState = Loading)
        }
        _quizScreenUIState.update {
            it.copy(
                quizRoundState = Error(
                    errorMessage
                )
            )
        }
    }

    fun onPokemonSelected(pokemon: Pokemon) {
        viewModelScope.launch(Dispatchers.Main) {
            val currentQuizRoundState = _quizScreenUIState.value.quizRoundState
            if (currentQuizRoundState is QuizRoundDataReady) {
                val updatedQuizScreenUIState = withContext(Dispatchers.IO) {
                    val updatedRoundState =
                        if (determineCorrectPokemonSelectedUseCase.execute(pokemon)) {
                            currentQuizRoundState.copy(quizAnswerState = Correct)
                        } else {
                            currentQuizRoundState.copy(quizAnswerState = Incorrect)
                        }

                    val currentScore = getScoreUseCase.execute()

                    _quizScreenUIState.value.copy(
                        quizRoundState = updatedRoundState,
                        score = currentScore
                    )
                }

                showResultAndLoadNextRound(updatedQuizScreenUIState)
            } else {
                throw IllegalStateException("Cannot check answer when round data is not ready")
            }
        }
    }

    private suspend fun showResultAndLoadNextRound(quizScreenUIState: QuizScreenUIState) {
        _quizScreenUIState.emit(quizScreenUIState)

        withContext(Dispatchers.IO) {
            delay(RESULT_DISPLAY_TIMEOUT)
        }

        loadQuizRoundData()
    }
}
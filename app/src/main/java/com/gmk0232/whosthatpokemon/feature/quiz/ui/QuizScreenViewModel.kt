package com.gmk0232.whosthatpokemon.feature.quiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmk0232.whosthatpokemon.feature.quiz.domain.DetermineCorrectPokemonSelectedUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState.Correct
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState.Incorrect
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizAnswerState.Unanswered
import com.gmk0232.whosthatpokemon.feature.quiz.ui.QuizRoundState.Loading
import com.gmk0232.whosthatpokemon.feature.quiz.ui.QuizRoundState.QuizRoundDataReady
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
) :
    ViewModel() {

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

    private fun loadQuizRoundData() {
        viewModelScope.launch(Dispatchers.Main) {

            _quizScreenUIState.emit(_quizScreenUIState.value.copy(quizRoundState = Loading))

            val quizRoundState = withContext(Dispatchers.IO) {
                QuizRoundDataReady(getPokemonQuizRoundDataUseCase.execute(), Unanswered)
            }

            _quizScreenUIState.emit((_quizScreenUIState.value.copy(quizRoundState = quizRoundState)))
        }
    }

    fun onPokemonSelected(pokemon: Pokemon) {
        viewModelScope.launch(Dispatchers.Main) {
            val currentQuizRoundState = _quizScreenUIState.value.quizRoundState
            if (currentQuizRoundState is QuizRoundDataReady) {
                val resultState = withContext(Dispatchers.IO) {
                    if (determineCorrectPokemonSelectedUseCase.execute(pokemon)) {
                        currentQuizRoundState.copy(quizAnswerState = Correct)
                    } else {
                        currentQuizRoundState.copy(quizAnswerState = Incorrect)
                    }
                }
                showResultAndLoadNextRound(resultState)
            }
        }
    }

    private suspend fun showResultAndLoadNextRound(resultState: QuizRoundState) {
        _quizScreenUIState.update { it.copy(quizRoundState = resultState) }

        withContext(Dispatchers.IO) {
            delay(RESULT_DISPLAY_TIMEOUT)
        }

        loadQuizRoundData()
    }
}
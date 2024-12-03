package com.gmk0232.whosthatpokemon.feature.quiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmk0232.whosthatpokemon.feature.quiz.domain.DetermineCorrectPokemonSelectedUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.FetchPokemonUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizRoundState
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizRoundState.Correct
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizRoundState.Incorrect
import com.gmk0232.whosthatpokemon.feature.quiz.domain.QuizRoundState.Unanswered
import com.gmk0232.whosthatpokemon.feature.quiz.ui.QuizScreenUIState.Loading
import com.gmk0232.whosthatpokemon.feature.quiz.ui.QuizScreenUIState.QuizRoundDataReady
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuizScreenViewModel @Inject constructor(
    private val getPokemonQuizRoundDataUseCase: GetPokemonQuizRoundDataUseCase,
    private val determineCorrectPokemonSelectedUseCase: DetermineCorrectPokemonSelectedUseCase,
    private val fetchPokemonUseCase: FetchPokemonUseCase
) :
    ViewModel() {

    private val _quizScreenUIState: MutableStateFlow<QuizScreenUIState> = MutableStateFlow(Loading)

    val quizScreenUIState = _quizScreenUIState.asStateFlow()

    init {
        loadQuizRoundData()
    }

    fun refresh() {
        loadQuizRoundData()
    }

    fun onPokemonSelected(pokemon: Pokemon) {
        viewModelScope.launch(Dispatchers.Main) {
            val currentState = _quizScreenUIState.value
            if (currentState is QuizRoundDataReady) {
                val resultState = withContext(Dispatchers.IO) {
                    if (determineCorrectPokemonSelectedUseCase.execute(pokemon)) {
                        currentState.copy(quizRoundState = Correct)
                    } else {
                        currentState.copy(quizRoundState = Incorrect)
                    }
                }
                showResultAndLoadNextRound(resultState)
            }
        }
    }

    private suspend fun showResultAndLoadNextRound(resultState: QuizScreenUIState) {
        _quizScreenUIState.emit(resultState)

        withContext(Dispatchers.IO) {
            delay(5000)
        }

        loadQuizRoundData()
    }

    private fun loadQuizRoundData() {
        viewModelScope.launch(Dispatchers.Main) {

            _quizScreenUIState.emit(Loading)

            val quizRoundData = withContext(Dispatchers.IO) {
                //fetchPokemonUseCase.execute()
                QuizRoundDataReady(getPokemonQuizRoundDataUseCase.execute(), Unanswered)
            }

            _quizScreenUIState.emit(quizRoundData)
        }
    }
}
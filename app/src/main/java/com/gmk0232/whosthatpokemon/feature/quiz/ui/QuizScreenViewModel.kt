package com.gmk0232.whosthatpokemon.feature.quiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmk0232.whosthatpokemon.feature.quiz.domain.DetermineCorrectPokemonSelectedUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCase
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
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
    private val determineCorrectPokemonSelectedUseCase: DetermineCorrectPokemonSelectedUseCase
) :
    ViewModel() {

    private val _quizScreenUIState: MutableStateFlow<QuizScreenUIState> = MutableStateFlow(Loading)

    val quizScreenUIState = _quizScreenUIState.asStateFlow()

    init {
        loadQuizRoundData()
    }

    fun onPokemonSelected(pokemon: Pokemon) {
        /*
        We could also pass the round data through the UI and avoid this state check but:
        -> This lets control when we can even determine the success state
        -> Maintains a single source of truth approach
        -> The UI is less complex
         */

        viewModelScope.launch(Dispatchers.Main) {
            val currentScreenState = quizScreenUIState.value

            if (currentScreenState is QuizRoundDataReady) {
                val roundState = withContext(Dispatchers.IO) {
                    if (determineCorrectPokemonSelectedUseCase.execute(pokemon)) {
                        Correct
                    } else {
                        Incorrect
                    }
                }

                _quizScreenUIState.emit(
                    currentScreenState.copy(
                        quizRoundState = roundState
                    )
                )
            }
        }


    }

    private fun loadQuizRoundData() {
        viewModelScope.launch(Dispatchers.Main) {

            _quizScreenUIState.emit(Loading)

            val quizRoundData = withContext(Dispatchers.IO) {
                delay(1000)
                QuizRoundDataReady(getPokemonQuizRoundDataUseCase.execute(), Unanswered)
            }

            _quizScreenUIState.emit(quizRoundData)
        }
    }
}
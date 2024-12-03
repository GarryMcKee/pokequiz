package com.gmk0232.whosthatpokemon.feature.quiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuizScreenViewModel @Inject constructor(private val getPokemonQuizRoundDataUseCase: GetPokemonQuizRoundDataUseCase) :
    ViewModel() {

    private val _quizScreenUIState: MutableStateFlow<QuizScreenUIState> = MutableStateFlow(
        QuizScreenUIState.QuizRoundDataReady(getPokemonQuizRoundDataUseCase.execute())
    )

    val quizScreenUIState = _quizScreenUIState.asStateFlow()

    init {
        loadQuizRoundData()
    }

    private fun loadQuizRoundData() {
        viewModelScope.launch(Dispatchers.Main) {

            _quizScreenUIState.emit(QuizScreenUIState.Loading)

            val quizRoundData = withContext(Dispatchers.IO) {
                delay(1000)
                QuizScreenUIState.QuizRoundDataReady(getPokemonQuizRoundDataUseCase.execute())
            }

            _quizScreenUIState.emit(quizRoundData)
        }
    }
}
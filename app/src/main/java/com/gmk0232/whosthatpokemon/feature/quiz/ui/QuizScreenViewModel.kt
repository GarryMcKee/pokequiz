package com.gmk0232.whosthatpokemon.feature.quiz.ui

import androidx.lifecycle.ViewModel
import com.gmk0232.whosthatpokemon.feature.quiz.domain.GetPokemonQuizRoundDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class QuizScreenViewModel @Inject constructor(private val getPokemonQuizRoundDataUseCase: GetPokemonQuizRoundDataUseCase) :
    ViewModel() {

    private val _quizScreenUIState: MutableStateFlow<QuizScreenUIState> = MutableStateFlow(
        QuizScreenUIState.QuizRoundDataReady(getPokemonQuizRoundDataUseCase.execute())
    )

    val quizScreenUIState = _quizScreenUIState.asStateFlow()


}
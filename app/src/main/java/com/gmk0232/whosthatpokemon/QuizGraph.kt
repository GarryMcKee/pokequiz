package com.gmk0232.whosthatpokemon

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gmk0232.whosthatpokemon.feature.quiz.ui.QuizRoute

const val QUIZ_ROUTE = "quizRoute"
fun NavGraphBuilder.quizNavGraph(
    navController: NavController
) {
    composable(route = QUIZ_ROUTE) {
        QuizRoute()
    }
}
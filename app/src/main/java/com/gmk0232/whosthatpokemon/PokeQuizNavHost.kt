package com.gmk0232.whosthatpokemon

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun PokeQuizNavHost(navController: NavHostController, innerPadding : PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = QUIZ_ROUTE,
        modifier = Modifier.padding(innerPadding)
    ) {
        quizNavGraph(navController)
    }
}
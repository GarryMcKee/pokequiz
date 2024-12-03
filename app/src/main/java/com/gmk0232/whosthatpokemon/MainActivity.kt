package com.gmk0232.whosthatpokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.gmk0232.whosthatpokemon.ui.theme.WhosThatPokemonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            WhosThatPokemonTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { QuizBar() }) { innerPadding ->
                    PokeQuizNavHost(navController, innerPadding)
                }
            }
        }
    }
}

@Composable
fun QuizBar() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Magenta)
        .padding(top = 16.dp, bottom = 16.dp)
        ) {
        Spacer(Modifier.weight(1f))
        Text("Who's that pokemon?")
        Spacer(Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun QuizBarPreview() {
    QuizBar()
}
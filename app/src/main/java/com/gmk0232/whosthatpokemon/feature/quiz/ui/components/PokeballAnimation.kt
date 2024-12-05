package com.gmk0232.whosthatpokemon.feature.quiz.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.gmk0232.whosthatpokemon.R
import com.gmk0232.whosthatpokemon.feature.quiz.ui.RESULT_DISPLAY_TIMEOUT

@Composable
fun PokeballAnimation() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Pokeball()

        var progress by remember { mutableStateOf(0f) }
        val progressAnimation by animateFloatAsState(
            targetValue = progress,
            animationSpec = tween(
                durationMillis = RESULT_DISPLAY_TIMEOUT.toInt(),
                easing = LinearEasing
            ), label = "showResultTimerAnimation"
        )

        LinearProgressIndicator(
            progress = { progressAnimation },
            trackColor = colorResource(R.color.white),
            color = colorResource(R.color.pokeball_red),
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .weight(1f)
                .clip(RoundedCornerShape(5.dp))
        )

        LaunchedEffect(LocalLifecycleOwner.current) {
            progress = 1F
        }
        Pokeball()
    }
}

@Composable
private fun Pokeball() {
    Image(
        imageVector = ImageVector.vectorResource(R.drawable.pokeball),
        "Pokeball",
        modifier = Modifier.size(20.dp)
    )
}
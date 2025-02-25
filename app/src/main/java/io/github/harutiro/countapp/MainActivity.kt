package io.github.harutiro.countapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.harutiro.countapp.ui.theme.CountAppTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CountAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CountAppScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CountAppScreen(modifier: Modifier = Modifier) {

    var count by remember { mutableIntStateOf(0) }
    val scale by animateFloatAsState(
        targetValue = if (count % 10 == 0 && count != 0) 1.5f else 1f,
        label = "scale"
    )
    val backgroundColor = remember { mutableStateOf(Color(0xFF1E1E1E)) }
    val achievementMessage = remember { mutableStateOf("") }

    LaunchedEffect(count) {
        if (count % 10 == 0 && count != 0) {
            backgroundColor.value = Color(
                Random.nextFloat(),
                Random.nextFloat(),
                Random.nextFloat(),
                1f)
        }

        if (count == 50) achievementMessage.value = "🎉 50回達成！ 🎉"
        if (count == 100) achievementMessage.value = "🏆 100回達成！ 🏆"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

    ) {
        Text(
            text = "カウント: $count",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .size(120.dp)
                .scale(scale),
            onClick = {
                count++
            }
        ) {
            Text(
                "+",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Text(
            text = achievementMessage.value,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Yellow
        )

        ShareButton(count)
    }
}

@Composable
fun ShareButton(count: Int) {
    val context = LocalContext.current

    Button(onClick = {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "今、$count 回カウントしました！ 🎉")
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "共有"))
    }) {
        Text("シェアする")
    }
}


@Preview
@Composable
fun CountAppScreenPreview() {
    CountAppTheme {
        CountAppScreen()
    }
}

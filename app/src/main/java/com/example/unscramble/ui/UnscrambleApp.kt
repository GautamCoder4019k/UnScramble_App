package com.example.unscramble.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscramble.R
import com.example.unscramble.ui.screen.GameScreen
import com.example.unscramble.ui.screen.GameViewModel

@Composable
fun UnscrambleApp() {
    Scaffold(topBar = { GameTopAppBar() }) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()

        ) {
            val gameViewModel: GameViewModel = viewModel(factory = GameViewModel.Factory)
            GameScreen(gameViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        )
    )
}
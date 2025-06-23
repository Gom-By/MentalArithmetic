package com.example.mentalarithmetic.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mentalarithmetic.R
import com.example.mentalarithmetic.domain.Difficulty
import com.example.mentalarithmetic.domain.QuizViewModel
import com.example.mentalarithmetic.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    vm: QuizViewModel = viewModel()
) {
    val topPlayers = vm.players.collectAsState()
    var showModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(64.dp))
        Card {
            Column(Modifier.padding(12.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(stringResource(R.string.app_name), style = Typography.headlineLarge, fontWeight = FontWeight.Medium)
                    Column(Modifier.padding(vertical = 16.dp)) {
                        Text(stringResource(R.string.rules_title), style = Typography.headlineMedium, fontWeight = Bold)
                        Text(stringResource(R.string.rule_1))
                        Text(stringResource(R.string.rule_2))
                        Text(stringResource(R.string.rule_3))
                        Text(stringResource(R.string.rule_4))
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        showModal = true
                    }) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = stringResource(R.string.start_icon_description))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.start_button))
                    }
                    Spacer(Modifier.width(16.dp))
                    OutlinedButton(onClick = {
                        navController.navigate("About")
                    }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = stringResource(R.string.about_icon_description))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.about_button))
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Card {
            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                Text(stringResource(R.string.scoreboard_title), style = Typography.headlineMedium, fontWeight = Bold)
                Spacer(Modifier.height(8.dp))
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(stringResource(R.string.column_name))
                            Text(stringResource(R.string.column_score))
                            Text(stringResource(R.string.column_lives_left))
                            Text(stringResource(R.string.column_difficulty))
                        }
                    }
                    items(items = topPlayers.value, itemContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(it.name)
                            Text(it.score.toString())
                            Text(it.lives.toString())
                            Text(it.gameMode.name)
                        }
                    })
                }
            }
        }

        if (showModal) {
            ModalBottomSheet(onDismissRequest = { showModal = false }) {
                Column(Modifier.padding(horizontal = 12.dp, vertical = 16.dp)) {
                    listOf(
                        Triple(Difficulty.EASY, R.string.easy_mode_title, R.string.easy_mode_description),
                        Triple(Difficulty.MEDIUM, R.string.medium_mode_title, R.string.medium_mode_description),
                        Triple(Difficulty.HARD, R.string.hard_mode_title, R.string.hard_mode_description),
                        Triple(Difficulty.EXPERT, R.string.expert_mode_title, R.string.expert_mode_description)
                    ).forEach { (difficulty, titleRes, descRes) ->
                        Row(
                            Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(stringResource(titleRes), style = Typography.headlineLarge)
                                Text(stringResource(descRes), style = Typography.labelLarge)
                            }
                            Button(onClick = {
                                showModal = false
                                vm.firstLaunch(difficulty)
                                navController.navigate("Quiz")
                            }) {
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
    val topPlayers = vm.players.collectAsState() // TODO : change to topPlayers
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
            Column(
                Modifier.padding(12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("Mental Arithmetic Game.", style = Typography.headlineLarge, fontWeight = FontWeight.Medium)
                    Column(Modifier.padding(vertical = 16.dp)) {
                        Text("Rules :", style = Typography.headlineMedium, fontWeight = Bold)
                        Text("A question will appear.")
                        Text("You have 20s to answer in the text input and then click \"Confirm\" to send the answer.")
                        Text("You can't redo a question after it has been already validate")
                        Text("Numbers are rounded up with 2 decimals")
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
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start")
                        Spacer(Modifier.width(8.dp))
                        Text("Start Now !")
                    }
                    Spacer(Modifier.width(16.dp))
                    OutlinedButton(onClick = {
                        navController.navigate("About")
                    }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "About")
                        Spacer(Modifier.width(8.dp))
                        Text("About")
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Card {
            Column(
                Modifier.fillMaxWidth().padding(12.dp)
            ) {
                Text("Scoreboard", style = Typography.headlineMedium, fontWeight = Bold)
                Spacer(Modifier.height(8.dp))
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Name")
                            Text("Score")
                            Text("Lives left")
                            Text("Difficulty")
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

        if(showModal) {
            ModalBottomSheet(
                onDismissRequest = {
                    showModal = false
                }) {
                Column(
                    Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Easy Mode", style = Typography.headlineLarge)
                            Text(
                                "Max lives, numbers between 0 and 10",
                                style = Typography.labelLarge
                            )
                        }
                        Button(onClick = {
                            showModal = false
                            vm.firstLaunch(difficulty = Difficulty.EASY)
                            navController.navigate("Quiz")
                        }) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Medium Mode", style = Typography.headlineLarge)
                            Text(
                                "More lives, numbers between 0 and 20",
                                style = Typography.labelLarge
                            )
                        }
                        Button(onClick = {
                            showModal = false
                            vm.firstLaunch(difficulty = Difficulty.MEDIUM)
                            navController.navigate("Quiz")
                        }) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Hard Mode", style = Typography.headlineLarge)
                            Text(
                                "Less lives, numbers between -20 and 20",
                                style = Typography.labelLarge
                            )
                        }
                        Button(onClick = {
                            showModal = false
                            vm.firstLaunch(difficulty = Difficulty.HARD)
                            navController.navigate("Quiz")
                        }) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Expert Mode", style = Typography.headlineLarge)
                            Text(
                                "3 lives, numbers between -100 and 100",
                                style = Typography.labelLarge
                            )
                        }
                        Button(onClick = {
                            showModal = false
                            vm.firstLaunch(difficulty = Difficulty.EXPERT)
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
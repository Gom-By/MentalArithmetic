package com.example.mentalarithmetic.presentation

import android.widget.Space
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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mentalarithmetic.domain.QuizViewModel
import com.example.mentalarithmetic.ui.theme.Typography

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    vm: QuizViewModel = viewModel()
) {
    val topPlayers = vm.topPlayers.collectAsState()
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
                    Text("This is a mental arithmetic game.", style = Typography.titleLarge)
                    Column(Modifier.padding(vertical = 16.dp)) {
                        Text("Rules :", style = Typography.titleMedium)
                        Text("After clicking start, a question will appear. Answer in the text input then click \"Confirm\" to send the answer.")
                        Text("You can't redo a question after it has been already validate")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        vm.firstLaunch()
                        navController.navigate("Quiz")
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
                Text("Previous scores saved :")
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
                            Text("Description")
                        }
                    }
                    items(items = players.value, itemContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(it.name)
                            Text(it.score.toString())
                            Text(it.lives.toString())
                        }
                    })
                }
            }
        }
    }
}
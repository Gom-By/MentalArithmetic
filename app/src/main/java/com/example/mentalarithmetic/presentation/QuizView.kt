package com.example.mentalarithmetic.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mentalarithmetic.domain.QuizViewModel
import com.example.mentalarithmetic.ui.theme.Typography

@Composable
fun QuizView(
    modifier: Modifier = Modifier,
    vm: QuizViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    var input by rememberSaveable { mutableStateOf("") }
    var showAnswer by rememberSaveable { mutableStateOf(false) }
    var isAnswerValid by rememberSaveable { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(64.dp))
        Row(
            Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Text("Difficulty : ${vm.gameDifficulty.value.name}")
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(imageVector = Icons.Default.Star, contentDescription = "Score")
                Spacer(Modifier.width(8.dp))
                Text(text = vm.score.value.toString())
            }
            Row {
                Text(text = vm.timerText.value)
            }
            Row {
                Text(text = vm.lives.value.toString())
                Spacer(Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Lives")
            }
        }
        Card {
            Column(
                modifier.padding(12.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(Modifier.fillMaxWidth()) { // the question
                    Text("Answer this question")
                    Text(text = "${vm.question.value} ${ if(showAnswer) vm.correctAnswer.value else "?" }",
                        style = Typography.displayLarge,
                        color = if(showAnswer) Color.Green else Color.Unspecified)
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth()) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = input,
                        onValueChange = { input = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                isAnswerValid = vm.confirmAnswer(input)
                            }
                        )
                    )
                }

                Spacer(Modifier.height(24.dp))
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    when (isAnswerValid) {
                        null -> {
                            Text("Try writing some numbers only.", color = Color.Yellow)
                        }
                        true -> {
                            val fixedAnswer by rememberSaveable { mutableStateOf(input) }
                            Text("$fixedAnswer is the correct answer. Bravo !", color = Color.Green)
                        }
                        false -> {
                            val fixedAnswer by rememberSaveable { mutableStateOf(input) }
                            Text("$fixedAnswer is a wrong answer :(", color = Color.Red)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth()) {
                    if (isAnswerValid != true) {
                        Button(onClick = {
                            isAnswerValid = vm.confirmAnswer(input)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Confirm Result"
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Check answer")
                        }
                        AnimatedVisibility(vm.score.value >= 3) {
                            Spacer(Modifier.width(12.dp))
                            OutlinedButton(onClick = {
                                showAnswer = true
                                vm.useHelp()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Show answer"
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Show answer")
                            }
                        }
                    } else {
                        Button(onClick = {
                            vm.generateQuestion()
                            input = ""
                            isAnswerValid = null
                            showAnswer = false
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next Question"
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Go Next !")
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            colors = ButtonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White,
                                disabledContainerColor = Color.Unspecified,
                                disabledContentColor = Color.Unspecified
                            ), onClick = {
                            vm.passQuestion()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Confirm Result"
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Pass (-1 live)")
                        }
                }
            }
        }

        if(vm.isGameTerminated.value) {
            Dialog(onDismissRequest = {
                navController.navigate("Home")
            }) {
                var playerName by rememberSaveable { mutableStateOf("") }
                Card {
                    Column(Modifier.padding(12.dp).fillMaxWidth()) {
                        Text("Game ended. Save the run ?")
                        Text("Enter name of the player")
                        TextField(
                            value = playerName,
                            onValueChange = { playerName = it }
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = {
                                navController.navigate("Home")
                                vm.registerPlayer(playerName)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Save"
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Save")
                            }
                            Spacer(Modifier.width(16.dp))
                            OutlinedButton(onClick = {
                                navController.navigate("Home")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cancel"
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}
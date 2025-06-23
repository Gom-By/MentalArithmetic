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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mentalarithmetic.R
import com.example.mentalarithmetic.domain.QuizViewModel
import com.example.mentalarithmetic.ui.theme.Typography

@Composable
fun QuizView(
    modifier: Modifier = Modifier,
    vm: QuizViewModel = viewModel(),
    navController: NavController = rememberNavController(),
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
        Row(Modifier
            .fillMaxWidth()
            .padding(12.dp)) {
            Text("${stringResource(R.string.difficulty_label)} ${vm.gameDifficulty.value.name}")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = stringResource(R.string.score_icon_description)
                )
                Spacer(Modifier.width(8.dp))
                Text(vm.score.value.toString())
            }
            Row { Text(vm.timerText.value) }
            Row {
                Text(vm.lives.value.toString())
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.lives_icon_description)
                )
            }
        }

        Card {
            Column(
                modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.question_title))
                    Text(
                        text = "${vm.question.value} ${if (showAnswer) vm.correctAnswer.value else "?"}",
                        style = Typography.displayLarge,
                        color = if (showAnswer) Color.Green else Color.Unspecified
                    )
                }

                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth()) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = input,
                        onValueChange = { input = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions {
                            isAnswerValid = vm.confirmAnswer(input)
                        }
                    )
                }

                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth()) {
                    when (isAnswerValid) {
                        null -> Text(stringResource(R.string.input_hint), color = Color.Yellow)
                        true -> {
                            val fixedAnswer by rememberSaveable { mutableStateOf(input) }
                            Text(
                                "$fixedAnswer ${stringResource(R.string.answer_correct_suffix)}",
                                color = Color.Green
                            )
                        }

                        false -> {
                            val fixedAnswer by rememberSaveable { mutableStateOf(input) }
                            Text(
                                "$fixedAnswer ${stringResource(R.string.answer_wrong_suffix)}",
                                color = Color.Red
                            )
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
                                contentDescription = stringResource(R.string.confirm_icon_description)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.confirm_button))
                        }
                        AnimatedVisibility(vm.score.value >= 3) {
                            Spacer(Modifier.width(12.dp))
                            OutlinedButton(onClick = {
                                showAnswer = true
                                vm.useHelp()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = stringResource(R.string.help_icon_description)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.help_button))
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
                                contentDescription = stringResource(R.string.next_icon_description)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.next_button))
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        onClick = { vm.passQuestion() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.confirm_icon_description)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.pass_button))
                    }
                }
            }
        }

        if (vm.isGameTerminated.value) {
            Dialog(onDismissRequest = {
                navController.navigate("Home")
            }) {
                var playerName by rememberSaveable { mutableStateOf("") }
                Card {
                    Column(Modifier
                        .padding(12.dp)
                        .fillMaxWidth()) {
                        Text(stringResource(R.string.game_end_message))
                        Text(stringResource(R.string.player_name_prompt))
                        TextField(value = playerName, onValueChange = { playerName = it })
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
                                    contentDescription = stringResource(R.string.save_icon_description)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.save_button))
                            }
                            Spacer(Modifier.width(16.dp))
                            OutlinedButton(onClick = {
                                navController.navigate("Home")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.cancel_icon_description)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.cancel_button))
                            }
                        }
                    }
                }
            }
        }
    }
}

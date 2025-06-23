package com.example.mentalarithmetic.presentation

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mentalarithmetic.R
import com.example.mentalarithmetic.domain.QuizViewModel
import java.util.Locale

@Composable
fun AboutView(
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel = viewModel(),
) {
    var isSectionVisible by remember { mutableStateOf(true) }
    var isRequiredSectionVisible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card {
            Column(Modifier.padding(12.dp)) {
                Text(stringResource(R.string.creators_label) + " " + stringResource(R.string.creator_name))
                Text(stringResource(R.string.github_label) + " " + stringResource(R.string.github_repo_link))

                Row {
                    Button(onClick = {
                        quizViewModel.updateLanguage("fr")
                    }) {
                        Text("Français")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        quizViewModel.updateLanguage("es")
                    }) {
                        Text("Espagnol")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        quizViewModel.updateLanguage("en")
                    }) {
                        Text("English")
                    }

                }
                Column(Modifier.padding(vertical = 16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isRequiredSectionVisible = !isRequiredSectionVisible },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.required_features_title))
                        Icon(
                            imageVector = if (isRequiredSectionVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    AnimatedVisibility(isRequiredSectionVisible) {
                        Column(Modifier.padding(vertical = 16.dp)) {
                            Text(stringResource(R.string.required_feature_quiz))
                            Text(stringResource(R.string.required_feature_about))
                            Text(stringResource(R.string.required_feature_home))
                            Text(stringResource(R.string.required_feature_save_score))
                        }
                    }
                }

                Column(Modifier.padding(vertical = 16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isSectionVisible = !isSectionVisible },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.additional_features_title))
                        Icon(
                            imageVector = if (isSectionVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    AnimatedVisibility(isSectionVisible) {
                        Column(Modifier.padding(vertical = 16.dp)) {
                            Text(stringResource(R.string.additional_feature_timer))
                            Text(stringResource(R.string.additional_feature_skip))
                            Text(stringResource(R.string.additional_feature_help))
                            Text(stringResource(R.string.additional_feature_score))
                            Text(stringResource(R.string.additional_feature_lives))
                            Text(stringResource(R.string.additional_feature_modes))
                        }
                    }
                }
            }
        }
    }
}

fun getString(context: Context, resId: Int, locale: String): String {
    val config = Configuration(context.resources.configuration)
    config.setLocale(Locale(locale))
    return context.createConfigurationContext(config).resources.getString(resId)
}

@Preview
@Composable
fun AboutPreview() {
    AboutView()
}
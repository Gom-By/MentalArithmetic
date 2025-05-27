package com.example.mentalarithmetic.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.unit.dp

@Composable
fun AboutView(
    modifier: Modifier = Modifier,
) {
    var isSectionVisible by remember {
        mutableStateOf(true)
    }
    var isRequiredSectionVisibile by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card {
            Column(
                Modifier.padding(12.dp)
            ) {
                Text("Creators : Guerby Naharro Group 2.2")
                Column(Modifier.padding(vertical = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            isRequiredSectionVisibile = !isRequiredSectionVisibile
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Required Features")
                        Icon(imageVector = if (isRequiredSectionVisibile) Icons.Default.KeyboardArrowUp  else Icons.Default.KeyboardArrowDown , contentDescription = null)
                    }
                    AnimatedVisibility(isRequiredSectionVisibile) {
                        Column(Modifier.padding(vertical = 16.dp)) {
                            Text("Quiz : where the game happens")
                            Text("About page: this page")
                            Text("Home Page")
                        }
                    }
                }
                Column(Modifier.padding(vertical = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            isSectionVisible = !isSectionVisible
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Additional Features")
                        Icon(imageVector = if (isSectionVisible) Icons.Default.KeyboardArrowUp  else Icons.Default.KeyboardArrowDown , contentDescription = null)
                    }
                    AnimatedVisibility(isSectionVisible) {
                        Column(Modifier.padding(vertical = 16.dp)) {
                            Text("Feature 1 : Null")
                        }
                    }
                }
            }
        }
    }
}
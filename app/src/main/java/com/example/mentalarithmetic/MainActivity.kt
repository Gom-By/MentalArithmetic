package com.example.mentalarithmetic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.mentalarithmetic.data.MentalArithmeticDB
import com.example.mentalarithmetic.domain.QuizViewModel
import com.example.mentalarithmetic.presentation.AboutView
import com.example.mentalarithmetic.presentation.HomeView
import com.example.mentalarithmetic.presentation.QuizView
import com.example.mentalarithmetic.ui.theme.MentalArithmeticTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {
    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            MentalArithmeticDB::class.java,
            "mental_database"
        ).build()
    }

    val quizViewModel: QuizViewModel by viewModels<QuizViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return QuizViewModel(db.MentalArithmeticDao()) as T
                }
            }
        }
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            MentalArithmeticTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
                            Text("Mental Arithmetic")
                        }, navigationIcon = {
                            IconButton(onClick = {
                                navController.navigate("Home")
                            }) {
                                Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                            }
                        })
                    }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        startDestination = "Home") {
                        composable("Home") {
                            HomeView(
                                navController = navController,
                                vm = quizViewModel
                            )
                        }
                        composable("Quiz") {
                            QuizView(
                                navController = navController,
                                vm = quizViewModel
                            )
                        }
                        composable("About") {
                            AboutView()
                        }
                    }
                }
            }
        }
    }
}

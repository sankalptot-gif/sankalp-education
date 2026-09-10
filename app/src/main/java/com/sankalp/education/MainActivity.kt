package com.sankalp.education

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sankalp.education.data.AuthRepository
import com.sankalp.education.ui.LoginScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SankalpEducationApp()
        }
    }
}

@Composable
fun SankalpEducationApp() {

    var loggedIn by remember {
        mutableStateOf(AuthRepository.isLoggedIn())
    }

    if (!loggedIn) {

        LoginScreen(
            onLoginSuccess = {
                loggedIn = true
            }
        )

    } else {

        HomeScreen(
            onLogout = {
                loggedIn = false
            }
        )
    }
}

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Sankalp Education",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Login successful"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                kotlinx.coroutines.CoroutineScope(
                    kotlinx.coroutines.Dispatchers.Main
                ).launch {
                    AuthRepository.logout()
                    onLogout()
                }
            }
        ) {
            Text("Logout")
        }
    }
}

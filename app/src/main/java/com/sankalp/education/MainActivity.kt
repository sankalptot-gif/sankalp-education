package com.sankalp.education

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
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
    androidx.compose.foundation.layout.Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {

        androidx.compose.material3.Text(
            text = "Sankalp Education",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.height(16.dp)
        )

        androidx.compose.material3.Text(
            text = "Login successful"
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.height(24.dp)
        )

        androidx.compose.material3.Button(
            onClick = {
                kotlinx.coroutines.CoroutineScope(
                    kotlinx.coroutines.Dispatchers.Main
                ).launch {
                    AuthRepository.logout()
                    onLogout()
                }
            }
        ) {
            androidx.compose.material3.Text("Logout")
        }
    }
}

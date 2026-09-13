package com.sankalp.education

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sankalp.education.data.AuthRepository
import com.sankalp.education.data.ProfileRepository
import com.sankalp.education.ui.LoginScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SankalpEducationApp()
                }
            }
        }
    }
}

@Composable
fun SankalpEducationApp() {
    var isLoggedIn by remember {
        mutableStateOf(AuthRepository.isLoggedIn())
    }

    if (isLoggedIn) {
        DashboardScreen(
            onLogout = {
                isLoggedIn = false
            }
        )
    } else {
        LoginScreen(
            onLoginSuccess = {
                isLoggedIn = true
            }
        )
    }
}

@Composable
fun DashboardScreen(
    onLogout: () -> Unit
) {
    var userEmail by remember {
        mutableStateOf("Loading...")
    }

    var userRole by remember {
        mutableStateOf("student")
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        userEmail = ProfileRepository.getCurrentUserEmail()
            ?: "Unknown user"

        userRole = ProfileRepository.getCurrentUserRole()
    }

    val normalizedRole = userRole.lowercase()

    val dashboardTitle = when (normalizedRole) {
        "admin" -> "Admin Dashboard"
        "partner" -> "Partner Dashboard"
        else -> "Student Dashboard"
    }

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

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = dashboardTitle,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Email: $userEmail"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Role: $userRole"
        )

        Spacer(modifier = Modifier.height(28.dp))

        when (normalizedRole) {
            "admin" -> {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Manage Admissions")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Manage Partners")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Manage Courses")
                }
            }

            "partner" -> {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("New Admission")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("My Students")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("My Commission")
                }
            }

            else -> {
                Text(
                    text = "Your student account is ready."
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedButton(
            onClick = {
                scope.launch {
                    ProfileRepository.logout()
                    onLogout()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

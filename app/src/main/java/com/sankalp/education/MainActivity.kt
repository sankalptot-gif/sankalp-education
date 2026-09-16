package com.sankalp.education

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.sankalp.education.ui.AdminAdmissionsScreen
import com.sankalp.education.ui.AdmissionsScreen
import com.sankalp.education.ui.CoursesScreen
import com.sankalp.education.ui.LoginScreen
import com.sankalp.education.ui.PartnerApplicationsScreen
import com.sankalp.education.ui.PartnersScreen
import com.sankalp.education.ui.StudentsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SankalpApp()
                }
            }
        }
    }
}

@Composable
fun SankalpApp() {
    var isLoggedIn by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf("dashboard") }

    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = {
                isLoggedIn = true
                currentScreen = "dashboard"
            }
        )
        return
    }

    when (currentScreen) {

        "dashboard" -> {
            DashboardScreen(
                onLogout = {
                    isLoggedIn = false
                    currentScreen = "dashboard"
                },
                onOpenScreen = { screen ->
                    currentScreen = screen
                }
            )
        }

        "partner_applications" -> {
            PartnerApplicationsScreen(
                onBack = {
                    currentScreen = "dashboard"
                }
            )
        }

        "partners" -> {
            PartnersScreen(
                onBack = {
                    currentScreen = "dashboard"
                }
            )
        }

        "admin_admissions" -> {
            AdminAdmissionsScreen(
                onBack = {
                    currentScreen = "dashboard"
                }
            )
        }

        "admissions" -> {
            AdmissionsScreen(
                onBack = {
                    currentScreen = "dashboard"
                }
            )
        }

        "courses" -> {
            CoursesScreen(
                onBack = {
                    currentScreen = "dashboard"
                }
            )
        }

        "students" -> {
            StudentsScreen(
                onBack = {
                    currentScreen = "dashboard"
                }
            )
        }

        else -> {
            DashboardScreen(
                onLogout = {
                    isLoggedIn = false
                    currentScreen = "dashboard"
                },
                onOpenScreen = { screen ->
                    currentScreen = screen
                }
            )
        }
    }
}

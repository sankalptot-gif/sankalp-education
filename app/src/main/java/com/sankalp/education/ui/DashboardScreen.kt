package com.sankalp.education.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    onOpenScreen: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Sankalp Education",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Admin Dashboard",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        DashboardButton(
            title = "Manage Partner Applications",
            onClick = {
                onOpenScreen("partner_applications")
            }
        )

        DashboardButton(
            title = "Manage Partners",
            onClick = {
                onOpenScreen("partners")
            }
        )

        DashboardButton(
            title = "Manage Admissions",
            onClick = {
                onOpenScreen("admin_admissions")
            }
        )

        DashboardButton(
            title = "View Admissions",
            onClick = {
                onOpenScreen("admissions")
            }
        )

        DashboardButton(
            title = "Manage Courses",
            onClick = {
                onOpenScreen("courses")
            }
        )

        DashboardButton(
            title = "Manage Students",
            onClick = {
                onOpenScreen("students")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
private fun DashboardButton(
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(text = title)
        }
    }
}

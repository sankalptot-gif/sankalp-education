package com.sankalp.education.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sankalp.education.data.PartnerApplication
import com.sankalp.education.data.PartnerApplicationRepository
import kotlinx.coroutines.launch

@Composable
fun PartnerApplicationsScreen(
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var applications by remember {
        mutableStateOf<List<PartnerApplication>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var selectedApplication by remember {
        mutableStateOf<PartnerApplication?>(null)
    }

    var showReviewDialog by remember {
        mutableStateOf(false)
    }

    fun loadApplications() {
        scope.launch {
            isLoading = true
            errorMessage = null

            val result = PartnerApplicationRepository.getApplications()

            result
                .onSuccess {
                    applications = it
                }
                .onFailure {
                    errorMessage = it.message
                        ?: "Partner applications load nahi ho paayi."
                }

            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadApplications()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onBack) {
                Text("Back")
            }

            Text(
                text = "Partner Applications",
                style = MaterialTheme.typography.titleLarge
            )

            TextButton(
                onClick = {
                    loadApplications()
                }
            ) {
                Text("Refresh")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoading -> {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Applications load ho rahi hain...")
                }
            }

            errorMessage != null -> {
                Column {
                    Text(
                        text = errorMessage
                            ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            loadApplications()
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }

            applications.isEmpty() -> {
                Text("Abhi koi partner application nahi hai.")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = applications,
                        key = { application ->
                            application.id
                                ?: application.application_number
                                ?: application.hashCode().toString()
                        }
                    ) { application ->
                        PartnerApplicationCard(
                            application = application,
                            onReview = {
                                selectedApplication = application
                                showReviewDialog = true
                            },
                            onDelete = {
                                val applicationId = application.id
                                    ?: return@PartnerApplicationCard

                                scope.launch {
                                    val result =
                                        PartnerApplicationRepository
                                            .deleteApplication(applicationId)

                                    result
                                        .onSuccess {
                                            loadApplications()
                                        }
                                        .onFailure {
                                            errorMessage = it.message
                                                ?: "Application delete nahi ho paayi."
                                        }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showReviewDialog && selectedApplication != null) {
        ReviewApplicationDialog(
            application = selectedApplication!!,
            onDismiss = {
                showReviewDialog = false
                selectedApplication = null
            },
            onSaved = {
                showReviewDialog = false
                selectedApplication = null
                loadApplications()
            }
        )
    }
}

@Composable
private fun PartnerApplicationCard(
    application: PartnerApplication,
    onReview: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = application.full_name
                ?: "Name unavailable",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Application No: ${
                application.application_number ?: "-"
            }"
        )

        Text(
            text = "Father Name: ${
                application.father_name ?: "-"
            }"
        )

        Text(
            text = "Mother Name: ${
                application.mother_name ?: "-"
            }"
        )

        Text(
            text = "Gender: ${
                application.gender ?: "-"
            }"
        )

        Text(
            text = "Date of Birth: ${
                application.date_of_birth ?: "-"
            }"
        )

        Text(
            text = "Email: ${
                application.email ?: "-"
            }"
        )

        Text(
            text = "Phone: ${
                application.phone ?: "-"
            }"
        )

        Text(
            text = "Address: ${
                application.address ?: "-"
            }"
        )

        Text(
            text = "Aadhaar: ${
                application.aadhaar_number ?: "-"
            }"
        )

        Text(
            text = "PAN: ${
                application.pan_number ?: "-"
            }"
        )

        Text(
            text = "Education: ${
                application.education_details ?: "-"
            }"
        )

        Text(
            text = "Status: ${
                application.status ?: "pending"
            }",
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onReview,
                modifier = Modifier.width(150.dp)
            ) {
                Text("Review")
            }

            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.width(110.dp)
            ) {
                Text("Delete")
            }
        }
    }
}

@Composable
private fun ReviewApplicationDialog(
    application: PartnerApplication,
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var selectedStatus by remember {
        mutableStateOf(application.status ?: "pending")
    }

    var adminNotes by remember {
        mutableStateOf(application.admin_notes ?: "")
    }

    var rejectionReason by remember {
        mutableStateOf(application.rejection_reason ?: "")
    }

    var isSaving by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    AlertDialog(
        onDismissRequest = {
            if (!isSaving) {
                onDismiss()
            }
        },
        title = {
            Text("Review Application")
        },
        text = {
            Column {
                Text(
                    text = application.full_name
                        ?: "Applicant",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Status")

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (selectedStatus == "pending") {
                        Button(
                            onClick = {
                                selectedStatus = "pending"
                            },
                            modifier = Modifier.width(100.dp)
                        ) {
                            Text("Pending")
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                selectedStatus = "pending"
                            },
                            modifier = Modifier.width(100.dp)
                        ) {
                            Text("Pending")
                        }
                    }

                    if (selectedStatus == "approved") {
                        Button(
                            onClick = {
                                selectedStatus = "approved"
                            },
                            modifier = Modifier.width(105.dp)
                        ) {
                            Text("Approved")
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                selectedStatus = "approved"
                            },
                            modifier = Modifier.width(105.dp)
                        ) {
                            Text("Approved")
                        }
                    }

                    if (selectedStatus == "rejected") {
                        Button(
                            onClick = {
                                selectedStatus = "rejected"
                            },
                            modifier = Modifier.width(100.dp)
                        ) {
                            Text("Rejected")
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                selectedStatus = "rejected"
                            },
                            modifier = Modifier.width(100.dp)
                        ) {
                            Text("Rejected")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = adminNotes,
                    onValueChange = {
                        adminNotes = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Admin Notes")
                    },
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = rejectionReason,
                    onValueChange = {
                        rejectionReason = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Rejection Reason")
                    },
                    minLines = 2
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val applicationId = application.id
                        ?: run {
                            errorMessage = "Application ID nahi mila."
                            return@Button
                        }

                    scope.launch {
                        isSaving = true
                        errorMessage = null

                        val result =
                            PartnerApplicationRepository
                                .updateApplicationStatus(
                                    id = applicationId,
                                    status = selectedStatus,
                                    adminNotes = adminNotes
                                        .ifBlank { null },
                                    rejectionReason = rejectionReason
                                        .ifBlank { null },
                                    reviewedBy = null
                                )

                        result
                            .onSuccess {
                                onSaved()
                            }
                            .onFailure {
                                errorMessage = it.message
                                    ?: "Application update nahi ho paaya."
                            }

                        isSaving = false
                    }
                },
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator()
                } else {
                    Text("Save")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSaving
            ) {
                Text("Cancel")
            }
        }
    )
}

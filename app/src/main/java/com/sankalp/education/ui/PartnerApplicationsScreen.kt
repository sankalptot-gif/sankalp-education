package com.sankalp.education.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    var applications by remember {
        mutableStateOf<List<PartnerApplication>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var selectedApplication by remember {
        mutableStateOf<PartnerApplication?>(null)
    }

    val scope = rememberCoroutineScope()

    fun loadApplications() {
        scope.launch {
            loading = true

            val result =
                PartnerApplicationRepository.getApplications()

            result
                .onSuccess {
                    applications = it
                    errorMessage = ""
                }
                .onFailure {
                    errorMessage = it.message
                        ?: "Applications load nahi ho paayi."
                }

            loading = false
        }
    }

    LaunchedEffect(Unit) {
        loadApplications()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Partner Applications",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedButton(
                onClick = onBack
            ) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (loading) {
            CircularProgressIndicator()
        } else if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        } else if (applications.isEmpty()) {
            Text("Abhi koi partner application available nahi hai.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(
                    items = applications,
                    key = {
                        it.id
                            ?: it.application_number
                            ?: it.full_name.orEmpty()
                    }
                ) { application ->

                    PartnerApplicationCard(
                        application = application,
                        onReview = {
                            selectedApplication = application
                        },
                        onDelete = {
                            val applicationId =
                                application.id
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

    selectedApplication?.let { application ->
        ReviewApplicationDialog(
            application = application,
            onDismiss = {
                selectedApplication = null
            },
            onUpdated = {
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = application.full_name ?: "Unnamed Applicant",
            style = MaterialTheme.typography.titleLarge
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
            text = "Email: ${application.email ?: "-"}"
        )

        Text(
            text = "Phone: ${application.phone ?: "-"}"
        )

        Text(
            text = "Education: ${
                application.education_details ?: "-"
            }"
        )

        Text(
            text = "Status: ${application.status ?: "pending"}"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onReview,
                modifier = Modifier.weight(1f)
            ) {
                Text("View / Review")
            }

            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.weight(1f)
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
    onUpdated: () -> Unit
) {
    var adminNotes by remember {
        mutableStateOf(application.admin_notes.orEmpty())
    }

    var rejectionReason by remember {
        mutableStateOf(application.rejection_reason.orEmpty())
    }

    var saving by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = {
            if (!saving) {
                onDismiss()
            }
        },
        title = {
            Text("Application Details")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Application No: ${
                    application.application_number ?: "-"
                }")

                Text("Full Name: ${
                    application.full_name ?: "-"
                }")

                Text("Father Name: ${
                    application.father_name ?: "-"
                }")

                Text("Mother Name: ${
                    application.mother_name ?: "-"
                }")

                Text("Gender: ${
                    application.gender ?: "-"
                }")

                Text("Date of Birth: ${
                    application.date_of_birth ?: "-"
                }")

                Text("Email: ${
                    application.email ?: "-"
                }")

                Text("Phone: ${
                    application.phone ?: "-"
                }")

                Text("Address: ${
                    application.address ?: "-"
                }")

                Text("Aadhaar: ${
                    application.aadhaar_number ?: "-"
                }")

                Text("PAN: ${
                    application.pan_number ?: "-"
                }")

                Text("Education: ${
                    application.education_details ?: "-"
                }")

                Text("Profile Photo: ${
                    application.profile_photo_path ?: "-"
                }")

                Text("Aadhaar Document: ${
                    application.aadhaar_document_path ?: "-"
                }")

                Text("PAN Document: ${
                    application.pan_document_path ?: "-"
                }")

                Text("Education Document: ${
                    application.education_document_path ?: "-"
                }")

                Text("Other Document: ${
                    application.other_document_path ?: "-"
                }")

                Text("Current Status: ${
                    application.status ?: "pending"
                }")

                OutlinedTextField(
                    value = adminNotes,
                    onValueChange = {
                        adminNotes = it
                    },
                    label = {
                        Text("Admin Notes")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = rejectionReason,
                    onValueChange = {
                        rejectionReason = it
                    },
                    label = {
                        Text("Rejection Reason")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Column {
                TextButton(
                    enabled = !saving,
                    onClick = {
                        val applicationId =
                            application.id ?: run {
                                errorMessage =
                                    "Application ID nahi mila."
                                return@TextButton
                            }

                        saving = true
                        errorMessage = ""

                        scope.launch {
                            val result =
                                PartnerApplicationRepository
                                    .updateApplicationStatus(
                                        id = applicationId,
                                        status = "approved",
                                        adminNotes = adminNotes
                                            .trim()
                                            .ifBlank { null },
                                        rejectionReason = null,
                                        reviewedBy = null
                                    )

                            saving = false

                            result
                                .onSuccess {
                                    onUpdated()
                                }
                                .onFailure {
                                    errorMessage = it.message
                                        ?: "Approve nahi ho paaya."
                                }
                        }
                    }
                ) {
                    Text("Approve")
                }

                TextButton(
                    enabled = !saving,
                    onClick = {
                        val applicationId =
                            application.id ?: run {
                                errorMessage =
                                    "Application ID nahi mila."
                                return@TextButton
                            }

                        if (rejectionReason.isBlank()) {
                            errorMessage =
                                "Reject karne ke liye reason likhiye."
                            return@TextButton
                        }

                        saving = true
                        errorMessage = ""

                        scope.launch {
                            val result =
                                PartnerApplicationRepository
                                    .updateApplicationStatus(
                                        id = applicationId,
                                        status = "rejected",
                                        adminNotes = adminNotes
                                            .trim()
                                            .ifBlank { null },
                                        rejectionReason = rejectionReason
                                            .trim(),
                                        reviewedBy = null
                                    )

                            saving = false

                            result
                                .onSuccess {
                                    onUpdated()
                                }
                                .onFailure {
                                    errorMessage = it.message
                                        ?: "Reject nahi ho paaya."
                                }
                        }
                    }
                ) {
                    Text("Reject")
                }
            }
        },
        dismissButton = {
            TextButton(
                enabled = !saving,
                onClick = onDismiss
            ) {
                Text("Close")
            }
        }
    )
}

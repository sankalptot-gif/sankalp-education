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
import com.sankalp.education.data.Admission
import com.sankalp.education.data.AdmissionRepository
import kotlinx.coroutines.launch

@Composable
fun AdminAdmissionsScreen(
    onBack: () -> Unit
) {
    var admissions by remember {
        mutableStateOf<List<Admission>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var selectedAdmission by remember {
        mutableStateOf<Admission?>(null)
    }

    val scope = rememberCoroutineScope()

    fun loadAdmissions() {
        scope.launch {
            loading = true
            errorMessage = ""

            val result = AdmissionRepository.getAdmissions()

            result
                .onSuccess {
                    admissions = it
                }
                .onFailure {
                    errorMessage = it.message
                        ?: "Admissions load nahi ho paayi."
                }

            loading = false
        }
    }

    LaunchedEffect(Unit) {
        loadAdmissions()
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
                text = "All Admissions",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedButton(
                onClick = onBack
            ) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when {
            loading -> {
                CircularProgressIndicator()
            }

            errorMessage.isNotBlank() -> {
                Column {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            loadAdmissions()
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }

            admissions.isEmpty() -> {
                Text("Abhi koi admission available nahi hai.")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    items(
                        items = admissions,
                        key = { admission ->
                            admission.id
                                ?: admission.admission_number
                                ?: admission.student_name.orEmpty()
                        }
                    ) { admission ->
                        AdmissionCard(
                            admission = admission,
                            onView = {
                                selectedAdmission = admission
                            },
                            onDelete = {
                                val admissionId = admission.id
                                    ?: return@AdmissionCard

                                scope.launch {
                                    val result =
                                        AdmissionRepository.deleteAdmission(
                                            admissionId
                                        )

                                    result
                                        .onSuccess {
                                            loadAdmissions()
                                        }
                                        .onFailure {
                                            errorMessage = it.message
                                                ?: "Admission delete nahi ho paayi."
                                        }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    selectedAdmission?.let { admission ->
        AdmissionDetailsDialog(
            admission = admission,
            onDismiss = {
                selectedAdmission = null
            },
            onUpdated = {
                selectedAdmission = null
                loadAdmissions()
            }
        )
    }
}

@Composable
private fun AdmissionCard(
    admission: Admission,
    onView: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = admission.student_name ?: "Unnamed Student",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Admission No: ${
                admission.admission_number ?: "-"
            }"
        )

        Text(
            text = "Course: ${
                admission.course_name ?: "-"
            }"
        )

        Text(
            text = "Admission Date: ${
                admission.admission_date ?: "-"
            }"
        )

        Text(
            text = "Partner ID: ${
                admission.partner_id ?: "-"
            }"
        )

        Text(
            text = "Status: ${
                admission.status ?: "pending"
            }"
        )

        Text(
            text = "Payment Status: ${
                admission.payment_status ?: "-"
            }"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onView,
                modifier = Modifier.width(160.dp)
            ) {
                Text("View / Update")
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
private fun AdmissionDetailsDialog(
    admission: Admission,
    onDismiss: () -> Unit,
    onUpdated: () -> Unit
) {
    var status by remember {
        mutableStateOf(admission.status ?: "pending")
    }

    var remarks by remember {
        mutableStateOf(admission.remarks.orEmpty())
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
            Text("Admission Details")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Admission No: ${
                        admission.admission_number ?: "-"
                    }"
                )

                Text(
                    text = "Student: ${
                        admission.student_name ?: "-"
                    }"
                )

                Text(
                    text = "Course: ${
                        admission.course_name ?: "-"
                    }"
                )

                Text(
                    text = "Admission Date: ${
                        admission.admission_date ?: "-"
                    }"
                )

                Text(
                    text = "Partner ID: ${
                        admission.partner_id ?: "-"
                    }"
                )

                Text(
                    text = "Commission Percent: ${
                        admission.commission_percent ?: 0.0
                    }%"
                )

                Text(
                    text = "Commission Amount: ${
                        admission.commission_amount ?: 0.0
                    }"
                )

                Text(
                    text = "Payment Status: ${
                        admission.payment_status ?: "-"
                    }"
                )

                OutlinedTextField(
                    value = status,
                    onValueChange = {
                        status = it
                    },
                    label = {
                        Text("Status")
                    },
                    placeholder = {
                        Text("pending / approved / rejected")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = remarks,
                    onValueChange = {
                        remarks = it
                    },
                    label = {
                        Text("Remarks")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
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
            TextButton(
                enabled = !saving,
                onClick = {
                    val admissionId = admission.id
                        ?: run {
                            errorMessage = "Admission ID nahi mila."
                            return@TextButton
                        }

                    if (status.isBlank()) {
                        errorMessage = "Status required hai."
                        return@TextButton
                    }

                    saving = true
                    errorMessage = ""

                    scope.launch {
                        val result =
                            AdmissionRepository.updateAdmissionStatus(
                                id = admissionId,
                                status = status.trim(),
                                remarks = remarks
                                    .trim()
                                    .ifBlank { null }
                            )

                        saving = false

                        result
                            .onSuccess {
                                onUpdated()
                            }
                            .onFailure {
                                errorMessage = it.message
                                    ?: "Admission update nahi ho paaya."
                            }
                    }
                }
            ) {
                Text(
                    text = if (saving) "Saving..." else "Save"
                )
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

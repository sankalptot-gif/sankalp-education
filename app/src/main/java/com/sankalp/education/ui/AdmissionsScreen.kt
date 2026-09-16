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
import com.sankalp.education.data.Admission
import com.sankalp.education.data.AdmissionRepository
import kotlinx.coroutines.launch

@Composable
fun AdmissionsScreen(
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

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    fun loadAdmissions() {
        scope.launch {
            loading = true
            errorMessage = ""

            val result = AdmissionRepository.getAdmissions()

            loading = false

            result
                .onSuccess {
                    admissions = it
                }
                .onFailure {
                    errorMessage = it.message
                        ?: "Admissions load nahi ho paaye."
                }
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
                text = "Admissions",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedButton(
                onClick = onBack
            ) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showAddDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Admission")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        }

        if (errorMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (
            !loading &&
            admissions.isEmpty() &&
            errorMessage.isBlank()
        ) {
            Text("Abhi koi admission available nahi hai.")
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(admissions) { admission ->
                AdmissionItem(admission = admission)
            }
        }
    }

    if (showAddDialog) {
        AddAdmissionDialog(
            onDismiss = {
                showAddDialog = false
            },
            onSaved = {
                showAddDialog = false
                loadAdmissions()
            }
        )
    }
}

@Composable
fun AdmissionItem(
    admission: Admission
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = admission.student_name
                ?: "Student name unavailable",
            style = MaterialTheme.typography.titleLarge
        )

        admission.admission_number?.let {
            Text("Admission No: $it")
        }

        admission.course_name?.let {
            Text("Course: $it")
        }

        admission.phone?.let {
            Text("Phone: $it")
        }

        admission.admission_date?.let {
            Text("Admission Date: $it")
        }

        admission.status?.let {
            Text("Status: $it")
        }

        admission.fee_amount?.let {
            Text("Total Fees: ₹$it")
        }

        admission.paid_amount?.let {
            Text("Paid Amount: ₹$it")
        }

        admission.pending_amount?.let {
            Text("Pending Amount: ₹$it")
        }

        admission.payment_status?.let {
            Text("Payment Status: $it")
        }

        admission.commission_amount?.let {
            Text("Commission: ₹$it")
        }

        admission.remarks?.let {
            Text("Remarks: $it")
        }
    }
}

@Composable
fun AddAdmissionDialog(
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    var admissionNumber by remember {
        mutableStateOf("")
    }

    var studentName by remember {
        mutableStateOf("")
    }

    var gender by remember {
        mutableStateOf("")
    }

    var fatherName by remember {
        mutableStateOf("")
    }

    var motherName by remember {
        mutableStateOf("")
    }

    var dateOfBirth by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var address by remember {
        mutableStateOf("")
    }

    var courseName by remember {
        mutableStateOf("")
    }

    var admissionDate by remember {
        mutableStateOf("")
    }

    var feeAmount by remember {
        mutableStateOf("")
    }

    var paidAmount by remember {
        mutableStateOf("")
    }

    var commissionPercent by remember {
        mutableStateOf("")
    }

    var remarks by remember {
        mutableStateOf("")
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
            Text("Add New Admission")
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    OutlinedTextField(
                        value = admissionNumber,
                        onValueChange = {
                            admissionNumber = it
                            errorMessage = ""
                        },
                        label = {
                            Text("Admission Number *")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = studentName,
                        onValueChange = {
                            studentName = it
                            errorMessage = ""
                        },
                        label = {
                            Text("Student Name *")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = gender,
                        onValueChange = {
                            gender = it
                        },
                        label = {
                            Text("Gender")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = fatherName,
                        onValueChange = {
                            fatherName = it
                        },
                        label = {
                            Text("Father Name")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = motherName,
                        onValueChange = {
                            motherName = it
                        },
                        label = {
                            Text("Mother Name")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = dateOfBirth,
                        onValueChange = {
                            dateOfBirth = it
                        },
                        label = {
                            Text("Date of Birth (YYYY-MM-DD)")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it
                        },
                        label = {
                            Text("Phone")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = {
                            Text("Email")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = {
                            address = it
                        },
                        label = {
                            Text("Address")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = courseName,
                        onValueChange = {
                            courseName = it
                        },
                        label = {
                            Text("Course Name")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = admissionDate,
                        onValueChange = {
                            admissionDate = it
                        },
                        label = {
                            Text("Admission Date (YYYY-MM-DD)")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = feeAmount,
                        onValueChange = {
                            feeAmount = it
                        },
                        label = {
                            Text("Total Fees")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = paidAmount,
                        onValueChange = {
                            paidAmount = it
                        },
                        label = {
                            Text("Paid Amount")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = commissionPercent,
                        onValueChange = {
                            commissionPercent = it
                        },
                        label = {
                            Text("Commission Percent")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = remarks,
                        onValueChange = {
                            remarks = it
                        },
                        label = {
                            Text("Remarks")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = !saving,
                onClick = {
                    if (admissionNumber.isBlank()) {
                        errorMessage = "Admission number required hai."
                        return@TextButton
                    }

                    if (studentName.isBlank()) {
                        errorMessage = "Student name required hai."
                        return@TextButton
                    }

                    if (
                        dateOfBirth.isNotBlank() &&
                        !dateOfBirth.matches(
                            Regex("\\d{4}-\\d{2}-\\d{2}")
                        )
                    ) {
                        errorMessage =
                            "Date of Birth YYYY-MM-DD format mein likhein."
                        return@TextButton
                    }

                    if (
                        admissionDate.isNotBlank() &&
                        !admissionDate.matches(
                            Regex("\\d{4}-\\d{2}-\\d{2}")
                        )
                    ) {
                        errorMessage =
                            "Admission Date YYYY-MM-DD format mein likhein."
                        return@TextButton
                    }

                    val totalFees = feeAmount
                        .toDoubleOrNull()
                        ?: 0.0

                    val paidFees = paidAmount
                        .toDoubleOrNull()
                        ?: 0.0

                    val commission = commissionPercent
                        .toDoubleOrNull()
                        ?: 0.0

                    if (totalFees < 0 || paidFees < 0 || commission < 0) {
                        errorMessage =
                            "Fees aur commission negative nahi ho sakte."
                        return@TextButton
                    }

                    if (paidFees > totalFees && totalFees > 0) {
                        errorMessage =
                            "Paid amount total fees se zyada nahi ho sakta."
                        return@TextButton
                    }

                    saving = true
                    errorMessage = ""

                    scope.launch {
                        val result = AdmissionRepository.addAdmission(
                            admissionNumber = admissionNumber,
                            studentName = studentName,
                            gender = gender,
                            fatherName = fatherName,
                            motherName = motherName,
                            dateOfBirth = dateOfBirth,
                            phone = phone,
                            email = email,
                            address = address,
                            courseName = courseName,
                            admissionDate = admissionDate,
                            feeAmount = totalFees,
                            paidAmount = paidFees,
                            commissionPercent = commission,
                            remarks = remarks
                        )

                        saving = false

                        result
                            .onSuccess {
                                onSaved()
                            }
                            .onFailure {
                                errorMessage = it.message
                                    ?: "Admission save nahi ho paaya."
                            }
                    }
                }
            ) {
                if (saving) {
                    CircularProgressIndicator()
                } else {
                    Text("Save")
                }
            }
        },
        dismissButton = {
            TextButton(
                enabled = !saving,
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

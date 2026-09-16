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
import com.sankalp.education.data.Partner
import com.sankalp.education.data.PartnerInsert
import com.sankalp.education.data.PartnerRepository
import kotlinx.coroutines.launch

@Composable
fun PartnersScreen(
    onBack: () -> Unit
) {
    var partners by remember {
        mutableStateOf<List<Partner>>(emptyList())
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

    fun loadPartners() {
        scope.launch {
            loading = true
            errorMessage = ""

            val result = PartnerRepository.getPartners()

            result
                .onSuccess {
                    partners = it
                }
                .onFailure {
                    errorMessage = it.message
                        ?: "Partners load nahi ho paaye."
                }

            loading = false
        }
    }

    LaunchedEffect(Unit) {
        loadPartners()
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
                text = "Partners",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedButton(
                onClick = onBack
            ) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                showAddDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Partner")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        } else if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        } else if (partners.isEmpty()) {
            Text("Abhi koi partner available nahi hai.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = partners,
                    key = {
                        it.id
                            ?: it.partner_id
                            ?: it.full_name.orEmpty()
                    }
                ) { partner ->

                    PartnerCard(
                        partner = partner,

                        onApprove = {
                            val partnerId =
                                partner.id ?: return@PartnerCard

                            scope.launch {
                                PartnerRepository
                                    .updatePartnerStatus(
                                        id = partnerId,
                                        status = "approved"
                                    )

                                loadPartners()
                            }
                        },

                        onReject = {
                            val partnerId =
                                partner.id ?: return@PartnerCard

                            scope.launch {
                                PartnerRepository
                                    .updatePartnerStatus(
                                        id = partnerId,
                                        status = "rejected"
                                    )

                                loadPartners()
                            }
                        },

                        onDelete = {
                            val partnerId =
                                partner.id ?: return@PartnerCard

                            scope.launch {
                                PartnerRepository
                                    .deletePartner(partnerId)

                                loadPartners()
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddPartnerDialog(
            onDismiss = {
                showAddDialog = false
            },
            onSaved = {
                showAddDialog = false
                loadPartners()
            }
        )
    }
}

@Composable
private fun PartnerCard(
    partner: Partner,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = partner.full_name ?: "Unnamed Partner",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Partner ID: ${partner.partner_id ?: "-"}"
        )

        Text(
            text = "Email: ${partner.email ?: "-"}"
        )

        Text(
            text = "Phone: ${partner.phone ?: "-"}"
        )

        Text(
            text = "Status: ${partner.status ?: "pending"}"
        )

        Text(
            text = "Commission: ${partner.commission_percent ?: 0.0}%"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onApprove,
                modifier = Modifier.width(140.dp)
            ) {
                Text("Approve")
            }

            OutlinedButton(
                onClick = onReject,
                modifier = Modifier.width(140.dp)
            ) {
                Text("Reject")
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedButton(
            onClick = onDelete,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete")
        }
    }
}

@Composable
private fun AddPartnerDialog(
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    var partnerId by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var motherName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var aadhaarNumber by remember { mutableStateOf("") }
    var panNumber by remember { mutableStateOf("") }
    var educationDetails by remember { mutableStateOf("") }

    var isSaving by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = {
            if (!isSaving) {
                onDismiss()
            }
        },

        title = {
            Text("Add New Partner")
        },

        text = {
            Column {
                OutlinedTextField(
                    value = partnerId,
                    onValueChange = { partnerId = it },
                    label = { Text("Partner ID") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fatherName,
                    onValueChange = { fatherName = it },
                    label = { Text("Father Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = motherName,
                    onValueChange = { motherName = it },
                    label = { Text("Mother Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = { Text("Date of Birth") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = aadhaarNumber,
                    onValueChange = { aadhaarNumber = it },
                    label = { Text("Aadhaar Number") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = panNumber,
                    onValueChange = { panNumber = it },
                    label = { Text("PAN Number") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = educationDetails,
                    onValueChange = { educationDetails = it },
                    label = { Text("Education Details") },
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
        },

        confirmButton = {
            Button(
                enabled = !isSaving,
                onClick = {
                    if (partnerId.isBlank()) {
                        errorMessage = "Partner ID zaroori hai"
                        return@Button
                    }

                    if (fullName.isBlank()) {
                        errorMessage = "Full Name zaroori hai"
                        return@Button
                    }

                    scope.launch {
                        isSaving = true
                        errorMessage = ""

                        val partner = PartnerInsert(
                            partner_id = partnerId.trim(),
                            full_name = fullName.trim(),
                            father_name = fatherName.ifBlank { null },
                            mother_name = motherName.ifBlank { null },
                            gender = gender.ifBlank { null },
                            date_of_birth = dateOfBirth.ifBlank { null },
                            email = email.ifBlank { null },
                            phone = phone.ifBlank { null },
                            address = address.ifBlank { null },
                            aadhaar_number =
                                aadhaarNumber.ifBlank { null },
                            pan_number =
                                panNumber.ifBlank { null },
                            education_details =
                                educationDetails.ifBlank { null },
                            status = "pending",
                            commission_percent = 0.0
                        )

                        PartnerRepository
                            .addPartner(partner)
                            .onSuccess {
                                onSaved()
                            }
                            .onFailure {
                                errorMessage =
                                    it.message
                                        ?: "Partner save nahi ho paaya."
                            }

                        isSaving = false
                    }
                }
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
                enabled = !isSaving,
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

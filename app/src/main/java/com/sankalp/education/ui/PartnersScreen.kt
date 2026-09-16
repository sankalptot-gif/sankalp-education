package com.sankalp.education.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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

            val result = PartnerRepository.getPartners()

            result
                .onSuccess {
                    partners = it
                    errorMessage = ""
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
                                PartnerRepository.updatePartnerStatus(
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
                                PartnerRepository.updatePartnerStatus(
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
                                PartnerRepository.deletePartner(partnerId)
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
    var partnerId by remember {
        mutableStateOf("")
    }

    var fullName by remember {
        mutableStateOf("")
    }

    var fatherName by remember {
        mutableStateOf("")
    }

    var motherName by remember {
        mutableStateOf("")
    }

    var gender by remember {
        mutableStateOf("")
    }

    var dateOfBirth by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var address by remember {
        mutableStateOf("")
    }

    var aadhaarNumber by remember {
        mutableStateOf("")
    }

    var panNumber by remember {
        mutableStateOf("")
    }

    var educationDetails by remember {
        mutableStateOf("")
    }

    var commissionPercent by remember {
        mutableStateOf("0")
    }

    var saving by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add New Partner")
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = partnerId,
                        onValueChange = {
                            partnerId = it
                            errorMessage = ""
                        },
                        label = {
                            Text("Partner ID *")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            errorMessage = ""
                        },
                        label = {
                            Text("Full Name *")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
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
                }

                item {
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
                }

                item {
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
                }

                item {
                    OutlinedTextField(
                        value = dateOfBirth,
                        onValueChange = {
                            dateOfBirth = it
                        },
                        label = {
                            Text("Date of Birth")
                        },
                        placeholder = {
                            Text("YYYY-MM-DD")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
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
                }

                item {
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
                }

                item {
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
                }

                item {
                    OutlinedTextField(
                        value = aadhaarNumber,
                        onValueChange = {
                            aadhaarNumber = it
                        },
                        label = {
                            Text("Aadhaar Number")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = panNumber,
                        onValueChange = {
                            panNumber = it
                        },
                        label = {
                            Text("PAN Number")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = educationDetails,
                        onValueChange = {
                            educationDetails = it
                        },
                        label = {
                            Text("Education Details")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
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
                }

                item {
                    if (errorMessage.isNotBlank()) {
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
                    if (partnerId.isBlank() || fullName.isBlank()) {
                        errorMessage =
                            "Partner ID aur Full Name required hain."
                        return@TextButton
                    }

                    val commission = commissionPercent.toDoubleOrNull()

                    if (commission == null) {
                        errorMessage =
                            "Commission percent valid number hona chahiye."
                        return@TextButton
                    }

                    saving = true
                    errorMessage = ""

                    scope.launch {
                        val result = PartnerRepository.addPartner(
                            PartnerInsert(
                                partner_id = partnerId.trim(),
                                full_name = fullName.trim(),
                                father_name = fatherName.trim()
                                    .ifBlank { null },
                                mother_name = motherName.trim()
                                    .ifBlank { null },
                                gender = gender.trim()
                                    .ifBlank { null },
                                date_of_birth = dateOfBirth.trim()
                                    .ifBlank { null },
                                email = email.trim()
                                    .ifBlank { null },
                                phone = phone.trim()
                                    .ifBlank { null },
                                address = address.trim()
                                    .ifBlank { null },
                                aadhaar_number = aadhaarNumber.trim()
                                    .ifBlank { null },
                                pan_number = panNumber.trim()
                                    .ifBlank { null },
                                education_details = educationDetails.trim()
                                    .ifBlank { null },
                                status = "pending",
                                commission_percent = commission
                            )
                        )

                        saving = false

                        result
                            .onSuccess {
                                onSaved()
                            }
                            .onFailure {
                                errorMessage = it.message
                                    ?: "Partner save nahi ho paaya."
                            }
                    }
                }
            ) {
                Text(
                    if (saving) "Saving..." else "Save"
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !saving
            ) {
                Text("Cancel")
            }
        }
    )
}

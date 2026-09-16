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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import com.sankalp.education.data.PartnerRepository
import kotlinx.coroutines.launch

@Composable
fun PartnersScreen(
    onBack: () -> Unit
) {
    var partners by remember {
        mutableStateOf<List<Partner>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    fun loadPartners() {
        scope.launch {
            isLoading = true
            errorMessage = null

            val result = PartnerRepository.getPartners()

            result.onSuccess {
                partners = it
            }.onFailure {
                errorMessage = it.message ?: "Partners load nahi ho paaye"
            }

            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadPartners()
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

            Button(onClick = { showAddDialog = true }) {
                Text("Add Partner")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Partners",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        errorMessage?.let {
            Text(text = it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (isLoading) {
            CircularProgressIndicator()
        } else if (partners.isEmpty()) {
            Text("Abhi koi partner nahi hai.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = partners,
                    key = { partner -> partner.id ?: partner.hashCode() }
                ) { partner ->
                    PartnerCard(
                        partner = partner,
                        onApprove = {
                            partner.id?.let { id ->
                                scope.launch {
                                    PartnerRepository
                                        .updatePartnerStatus(id, "approved")
                                        .onSuccess { loadPartners() }
                                }
                            }
                        },
                        onReject = {
                            partner.id?.let { id ->
                                scope.launch {
                                    PartnerRepository
                                        .updatePartnerStatus(id, "rejected")
                                        .onSuccess { loadPartners() }
                                }
                            }
                        },
                        onDelete = {
                            partner.id?.let { id ->
                                scope.launch {
                                    PartnerRepository
                                        .deletePartner(id)
                                        .onSuccess { loadPartners() }
                                }
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
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text("Name: ${partner.name ?: "-"}")
            Text("Email: ${partner.email ?: "-"}")
            Text("Phone: ${partner.phone ?: "-"}")
            Text("Business: ${partner.business_name ?: "-"}")
            Text("City: ${partner.city ?: "-"}")
            Text("Status: ${partner.status ?: "-"}")

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onApprove,
                    modifier = Modifier.width(110.dp)
                ) {
                    Text("Approve")
                }

                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.width(110.dp)
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
}

@Composable
private fun AddPartnerDialog(
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Partner")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
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
                    value = businessName,
                    onValueChange = { businessName = it },
                    label = { Text("Business Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )

                errorMessage?.let {
                    Text(it)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Name zaroori hai"
                        return@Button
                    }

                    scope.launch {
                        isSaving = true
                        errorMessage = null

                        PartnerRepository.addPartner(
                            name = name,
                            email = email.ifBlank { null },
                            phone = phone.ifBlank { null },
                            businessName = businessName.ifBlank { null },
                            city = city.ifBlank { null }
                        ).onSuccess {
                            onSaved()
                        }.onFailure {
                            errorMessage =
                                it.message ?: "Partner save nahi ho paaya"
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
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

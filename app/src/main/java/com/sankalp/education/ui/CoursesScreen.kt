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
import com.sankalp.education.data.Course
import com.sankalp.education.data.CourseRepository
import kotlinx.coroutines.launch

@Composable
fun CoursesScreen(
    onBack: () -> Unit
) {
    var courses by remember {
        mutableStateOf<List<Course>>(emptyList())
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

    fun loadCourses() {
        scope.launch {
            loading = true
            errorMessage = ""

            val result = CourseRepository.getCourses()

            loading = false

            result
                .onSuccess {
                    courses = it
                }
                .onFailure {
                    errorMessage = it.message ?: "Courses load nahi ho paaye."
                }
        }
    }

    LaunchedEffect(Unit) {
        loadCourses()
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
                text = "Courses",
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
            Text("Add New Course")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        }

        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (!loading && courses.isEmpty() && errorMessage.isBlank()) {
            Text(
                text = "Abhi koi course available nahi hai."
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(courses) { course ->
                CourseItem(course = course)
            }
        }
    }

    if (showAddDialog) {
        AddCourseDialog(
            onDismiss = {
                showAddDialog = false
            },
            onSaved = {
                showAddDialog = false
                loadCourses()
            }
        )
    }
}

@Composable
fun CourseItem(
    course: Course
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = course.name,
            style = MaterialTheme.typography.titleLarge
        )

        course.code?.let {
            Text(text = "Code: $it")
        }

        course.duration?.let {
            Text(text = "Duration: $it")
        }

        course.fees?.let {
            Text(text = "Fees: ₹$it")
        }

        course.description?.let {
            if (it.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it)
            }
        }
    }
}

@Composable
fun AddCourseDialog(
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }

    var code by remember {
        mutableStateOf("")
    }

    var duration by remember {
        mutableStateOf("")
    }

    var fees by remember {
        mutableStateOf("")
    }

    var description by remember {
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
            Text("Add New Course")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text("Course Name *")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = code,
                    onValueChange = {
                        code = it
                    },
                    label = {
                        Text("Course Code")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = duration,
                    onValueChange = {
                        duration = it
                    },
                    label = {
                        Text("Duration")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fees,
                    onValueChange = {
                        fees = it
                    },
                    label = {
                        Text("Fees")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("Description")
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
        },
        confirmButton = {
            TextButton(
                enabled = !saving,
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Course name required hai."
                        return@TextButton
                    }

                    val feeAmount = fees.toDoubleOrNull() ?: 0.0

                    saving = true
                    errorMessage = ""

                    scope.launch {
                        val result = CourseRepository.addCourse(
                            name = name.trim(),
                            code = code.trim(),
                            duration = duration.trim(),
                            fees = feeAmount,
                            description = description.trim()
                        )

                        saving = false

                        result
                            .onSuccess {
                                onSaved()
                            }
                            .onFailure {
                                errorMessage =
                                    it.message ?: "Course save nahi ho paaya."
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

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sankalp.education.data.Student
import com.sankalp.education.data.StudentRepository
import kotlinx.coroutines.launch

@Composable
fun StudentsScreen(
    onBack: () -> Unit
) {
    var students by remember {
        mutableStateOf<List<Student>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    fun loadStudents() {
        scope.launch {
            loading = true
            errorMessage = ""

            val result = StudentRepository.getStudents()

            loading = false

            result
                .onSuccess {
                    students = it
                }
                .onFailure {
                    errorMessage = it.message
                        ?: "Students load nahi ho paaye."
                }
        }
    }

    LaunchedEffect(Unit) {
        loadStudents()
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
                text = "Students",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedButton(
                onClick = onBack
            ) {
                Text("Back")
            }
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

        if (!loading &&
            students.isEmpty() &&
            errorMessage.isBlank()
        ) {
            Text(
                text = "Abhi koi student available nahi hai."
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(students) { student ->
                StudentItem(student = student)
            }
        }
    }
}

@Composable
fun StudentItem(
    student: Student
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = student.full_name ?: "Name not available",
            style = MaterialTheme.typography.titleLarge
        )

        student.email?.let {
            Text(text = "Email: $it")
        }

        student.phone?.let {
            Text(text = "Phone: $it")
        }

        student.course_id?.let {
            Text(text = "Course ID: $it")
        }

        student.created_at?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Created: $it")
        }
    }
}

package com.sankalp.education.data

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String? = null,
    val user_id: String? = null,
    val admission_number: String? = null,
    val full_name: String? = null,
    val father_name: String? = null,
    val mother_name: String? = null,
    val date_of_birth: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val course: String? = null,
    val batch: String? = null,
    val admission_date: String? = null,
    val photo_url: String? = null,
    val status: String? = null
)

@Serializable
data class StudentInsert(
    val admission_number: String,
    val full_name: String,
    val father_name: String? = null,
    val mother_name: String? = null,
    val date_of_birth: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val course: String? = null,
    val batch: String? = null,
    val admission_date: String? = null,
    val photo_url: String? = null,
    val status: String = "active"
)

object StudentRepository {

    private val supabase
        get() = SupabaseClientProvider.client

    suspend fun getStudents(): Result<List<Student>> {
        return try {
            val students = supabase
                .from("students")
                .select()
                .decodeList<Student>()

            Result.success(students)
        } catch (e: Exception) {
            Result.failure(
                Exception("Students load nahi ho paaye.")
            )
        }
    }

    suspend fun addStudent(
        admissionNumber: String,
        fullName: String,
        fatherName: String,
        motherName: String,
        dateOfBirth: String,
        phone: String,
        email: String,
        address: String,
        course: String,
        batch: String,
        admissionDate: String
    ): Result<Unit> {
        return try {
            val student = StudentInsert(
                admission_number = admissionNumber.trim(),
                full_name = fullName.trim(),
                father_name = fatherName.trim().ifBlank { null },
                mother_name = motherName.trim().ifBlank { null },
                date_of_birth = dateOfBirth.trim().ifBlank { null },
                phone = phone.trim().ifBlank { null },
                email = email.trim().ifBlank { null },
                address = address.trim().ifBlank { null },
                course = course.trim().ifBlank { null },
                batch = batch.trim().ifBlank { null },
                admission_date = admissionDate.trim().ifBlank { null },
                status = "active"
            )

            supabase
                .from("students")
                .insert(student)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                Exception("Student save nahi ho paaya. Please details check karein.")
            )
        }
    }
}

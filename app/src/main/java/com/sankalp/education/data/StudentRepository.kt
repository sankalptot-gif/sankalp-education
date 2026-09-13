package com.sankalp.education.data

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String? = null,
    val full_name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val course_id: String? = null,
    val created_at: String? = null
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
}

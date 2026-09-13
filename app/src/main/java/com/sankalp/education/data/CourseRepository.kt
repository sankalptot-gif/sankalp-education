package com.sankalp.education.data

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String? = null,
    val name: String,
    val code: String? = null,
    val description: String? = null,
    val duration: String? = null,
    val fee: Double? = null,
    val status: String? = null,
    val created_at: String? = null
)

object CourseRepository {

    private val supabase
        get() = SupabaseClientProvider.client

    suspend fun getCourses(): Result<List<Course>> {
        return try {
            val courses = supabase
                .from("courses")
                .select()
                .decodeList<Course>()

            Result.success(courses)
        } catch (e: Exception) {
            Result.failure(
                Exception("Courses load nahi ho paaye.")
            )
        }
    }

    suspend fun addCourse(
        name: String,
        code: String,
        duration: String,
        fees: Double,
        description: String
    ): Result<Unit> {
        return try {
            val course = Course(
                name = name,
                code = code.ifBlank { null },
                description = description.ifBlank { null },
                duration = duration.ifBlank { null },
                fee = fees,
                status = "active"
            )

            supabase
                .from("courses")
                .insert(course)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                Exception("Course save nahi ho paaya. Please try again.")
            )
        }
    }
}

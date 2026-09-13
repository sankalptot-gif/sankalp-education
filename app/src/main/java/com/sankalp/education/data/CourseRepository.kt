package com.sankalp.education.data

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String? = null,
    val name: String,
    val code: String? = null,
    val duration: String? = null,
    val fees: Double? = null,
    val description: String? = null,
    val is_active: Boolean = true
)

object CourseRepository {

    private val supabase
        get() = SupabaseClientProvider.client

    suspend fun getCourses(): Result<List<Course>> {
        return try {
            val courses = supabase
                .from("courses")
                .select {
                    filter {
                        eq("is_active", true)
                    }
                }
                .decodeList<Course>()

            Result.success(courses)
        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Courses load nahi ho paaye.")
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
                duration = duration.ifBlank { null },
                fees = fees,
                description = description.ifBlank { null },
                is_active = true
            )

            supabase
                .from("courses")
                .insert(course)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Course save nahi ho paaya.")
            )
        }
    }
}

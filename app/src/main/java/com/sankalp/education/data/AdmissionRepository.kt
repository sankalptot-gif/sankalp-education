package com.sankalp.education.data

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class Admission(
    val id: String? = null,
    val admission_number: String? = null,
    val partner_id: String? = null,
    val student_id: String? = null,
    val student_name: String? = null,
    val course_name: String? = null,
    val admission_date: String? = null,
    val status: String? = null,
    val remarks: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val commission_percent: Double? = null,
    val commission_amount: Double? = null,
    val payment_status: String? = null
)

object AdmissionRepository {

    private val supabase
        get() = SupabaseClientProvider.client

    suspend fun getAdmissions(): Result<List<Admission>> {
        return try {
            val admissions = supabase
                .from("admissions")
                .select()
                .decodeList<Admission>()

            Result.success(admissions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAdmissionStatus(
        id: String,
        status: String,
        remarks: String? = null
    ): Result<Unit> {
        return try {
            supabase
                .from("admissions")
                .update(
                    mapOf(
                        "status" to status,
                        "remarks" to remarks
                    )
                ) {
                    filter {
                        eq("id", id)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAdmission(
        id: String
    ): Result<Unit> {
        return try {
            supabase
                .from("admissions")
                .delete {
                    filter {
                        eq("id", id)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

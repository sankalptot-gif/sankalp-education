package com.sankalp.education.data

import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

@Serializable
data class PartnerApplication(
    val id: String? = null,
    val application_number: String? = null,
    val full_name: String? = null,
    val father_name: String? = null,
    val mother_name: String? = null,
    val gender: String? = null,
    val date_of_birth: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val aadhaar_number: String? = null,
    val pan_number: String? = null,
    val education_details: String? = null,
    val profile_photo_path: String? = null,
    val aadhaar_document_path: String? = null,
    val pan_document_path: String? = null,
    val education_document_path: String? = null,
    val other_document_path: String? = null,
    val status: String? = null,
    val admin_notes: String? = null,
    val rejection_reason: String? = null,
    val reviewed_by: String? = null,
    val reviewed_at: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val user_id: String? = null
)

object PartnerApplicationRepository {

    private val supabase
        get() = SupabaseClientProvider.client

    suspend fun getApplications(): Result<List<PartnerApplication>> {
        return try {
            val applications = supabase
                .from("partner_applications")
                .select()
                .decodeList<PartnerApplication>()

            Result.success(applications)
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    e.message ?: "Partner applications load nahi ho paayi."
                )
            )
        }
    }

    suspend fun updateApplicationStatus(
        id: String,
        status: String,
        adminNotes: String? = null,
        rejectionReason: String? = null,
        reviewedBy: String? = null
    ): Result<Unit> {
        return try {
            val updateData = mutableMapOf<String, String?>(
                "status" to status,
                "admin_notes" to adminNotes,
                "rejection_reason" to rejectionReason,
                "reviewed_by" to reviewedBy
            )

            supabase
                .from("partner_applications")
                .update(updateData) {
                    filter {
                        eq("id", id)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    e.message ?: "Application status update nahi ho paaya."
                )
            )
        }
    }

    suspend fun deleteApplication(
        id: String
    ): Result<Unit> {
        return try {
            supabase
                .from("partner_applications")
                .delete {
                    filter {
                        eq("id", id)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    e.message ?: "Application delete nahi ho paayi."
                )
            )
        }
    }
}

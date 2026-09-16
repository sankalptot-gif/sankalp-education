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
    val gender: String? = null,
    val father_name: String? = null,
    val mother_name: String? = null,
    val date_of_birth: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val course_name: String? = null,
    val admission_date: String? = null,
    val status: String? = null,
    val fee_amount: Double? = null,
    val paid_amount: Double? = null,
    val pending_amount: Double? = null,
    val commission_percent: Double? = null,
    val commission_amount: Double? = null,
    val payment_status: String? = null,
    val remarks: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

@Serializable
data class AdmissionInsert(
    val admission_number: String,
    val partner_id: String? = null,
    val student_id: String? = null,
    val student_name: String,
    val gender: String? = null,
    val father_name: String? = null,
    val mother_name: String? = null,
    val date_of_birth: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val course_name: String? = null,
    val admission_date: String? = null,
    val status: String = "pending",
    val fee_amount: Double = 0.0,
    val paid_amount: Double = 0.0,
    val pending_amount: Double = 0.0,
    val commission_percent: Double = 0.0,
    val commission_amount: Double = 0.0,
    val payment_status: String = "pending",
    val remarks: String? = null
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
            Result.failure(
                Exception(
                    e.message ?: "Admissions load nahi ho paaye."
                )
            )
        }
    }

    suspend fun addAdmission(
        admissionNumber: String,
        studentName: String,
        gender: String,
        fatherName: String,
        motherName: String,
        dateOfBirth: String,
        phone: String,
        email: String,
        address: String,
        courseName: String,
        admissionDate: String,
        feeAmount: Double,
        paidAmount: Double,
        commissionPercent: Double,
        remarks: String
    ): Result<Unit> {
        return try {
            val pendingAmount = (feeAmount - paidAmount).coerceAtLeast(0.0)

            val commissionAmount =
                feeAmount * commissionPercent / 100.0

            val paymentStatus = when {
                paidAmount <= 0.0 -> "pending"
                paidAmount >= feeAmount && feeAmount > 0.0 -> "paid"
                else -> "partial"
            }

            val admission = AdmissionInsert(
                admission_number = admissionNumber.trim(),
                student_name = studentName.trim(),
                gender = gender.trim().ifBlank { null },
                father_name = fatherName.trim().ifBlank { null },
                mother_name = motherName.trim().ifBlank { null },
                date_of_birth = dateOfBirth.trim().ifBlank { null },
                phone = phone.trim().ifBlank { null },
                email = email.trim().ifBlank { null },
                address = address.trim().ifBlank { null },
                course_name = courseName.trim().ifBlank { null },
                admission_date = admissionDate.trim().ifBlank { null },
                status = "pending",
                fee_amount = feeAmount,
                paid_amount = paidAmount,
                pending_amount = pendingAmount,
                commission_percent = commissionPercent,
                commission_amount = commissionAmount,
                payment_status = paymentStatus,
                remarks = remarks.trim().ifBlank { null }
            )

            supabase
                .from("admissions")
                .insert(admission)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    e.message
                        ?: "Admission save nahi ho paaya."
                )
            )
        }
    }
}

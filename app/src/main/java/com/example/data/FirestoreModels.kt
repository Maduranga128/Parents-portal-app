package com.example.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FirestoreDocumentsResponse(
    val documents: List<FirestoreDocumentResponse>?
)

@JsonClass(generateAdapter = true)
data class FirestoreDocumentResponse(
    val name: String,
    val fields: FirestoreFields,
    val createTime: String?,
    val updateTime: String?
)

@JsonClass(generateAdapter = true)
data class FirestoreFields(
    val studentName: FirestoreValueDto? = null,
    val studentClass: FirestoreValueDto? = null,
    val parentName: FirestoreValueDto? = null,
    val job: FirestoreValueDto? = null,
    val support: FirestoreValueDto? = null,
    val phone: FirestoreValueDto? = null,
    val address: FirestoreValueDto? = null,
    val timestamp: FirestoreValueDto? = null
)

@JsonClass(generateAdapter = true)
data class FirestoreValueDto(
    val stringValue: String?
)

@JsonClass(generateAdapter = true)
data class FirestoreDocumentRequest(
    val fields: FirestoreFields
)

/**
 * Maps a Firestore document JSON response directly to a clean local Room database Entity.
 */
fun FirestoreDocumentResponse.toEntity(): ParentEntity {
    val documentId = name.substringAfterLast("/")
    return ParentEntity(
        id = documentId,
        studentName = fields.studentName?.stringValue ?: "",
        studentClass = fields.studentClass?.stringValue ?: "",
        parentName = fields.parentName?.stringValue ?: "",
        job = fields.job?.stringValue ?: "",
        support = fields.support?.stringValue ?: "",
        phone = fields.phone?.stringValue ?: "",
        address = fields.address?.stringValue ?: "",
        timestamp = fields.timestamp?.stringValue ?: "",
        isSynced = true
    )
}

/**
 * Generates a clean Firestore request payload from custom entry fields.
 */
fun createFirestoreFields(
    studentName: String,
    studentClass: String,
    parentName: String,
    job: String,
    support: String,
    phone: String,
    address: String,
    timestamp: String
): FirestoreFields {
    return FirestoreFields(
        studentName = FirestoreValueDto(studentName),
        studentClass = FirestoreValueDto(studentClass),
        parentName = FirestoreValueDto(parentName),
        job = FirestoreValueDto(job),
        support = FirestoreValueDto(support),
        phone = FirestoreValueDto(phone),
        address = FirestoreValueDto(address),
        timestamp = FirestoreValueDto(timestamp)
    )
}
